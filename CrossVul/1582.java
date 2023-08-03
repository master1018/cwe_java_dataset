
package org.jboss.weld.servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.api.Service;
import org.jboss.weld.context.cache.RequestScopedCache;
import org.jboss.weld.context.http.HttpRequestContext;
import org.jboss.weld.context.http.HttpRequestContextImpl;
import org.jboss.weld.context.http.HttpSessionContext;
import org.jboss.weld.context.http.HttpSessionDestructionContext;
import org.jboss.weld.event.FastEvent;
import org.jboss.weld.literal.DestroyedLiteral;
import org.jboss.weld.literal.InitializedLiteral;
import org.jboss.weld.logging.ServletLogger;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.servlet.spi.HttpContextActivationFilter;
import org.jboss.weld.util.reflection.Reflections;
public class HttpContextLifecycle implements Service {
    private static final String HTTP_SESSION = "org.jboss.weld." + HttpSession.class.getName();
    private static final String INCLUDE_HEADER = "javax.servlet.include.request_uri";
    private static final String FORWARD_HEADER = "javax.servlet.forward.request_uri";
    private static final String REQUEST_DESTROYED = HttpContextLifecycle.class.getName() + ".request.destroyed";
    private static final String GUARD_PARAMETER_NAME = "org.jboss.weld.context.ignore.guard.marker";
    private static final Object GUARD_PARAMETER_VALUE = new Object();
    private HttpSessionDestructionContext sessionDestructionContextCache;
    private HttpSessionContext sessionContextCache;
    private HttpRequestContext requestContextCache;
    private volatile Boolean conversationActivationEnabled;
    private final boolean ignoreForwards;
    private final boolean ignoreIncludes;
    private final BeanManagerImpl beanManager;
    private final ConversationContextActivator conversationContextActivator;
    private final HttpContextActivationFilter contextActivationFilter;
    private final FastEvent<ServletContext> applicationInitializedEvent;
    private final FastEvent<ServletContext> applicationDestroyedEvent;
    private final FastEvent<HttpServletRequest> requestInitializedEvent;
    private final FastEvent<HttpServletRequest> requestDestroyedEvent;
    private final FastEvent<HttpSession> sessionInitializedEvent;
    private final FastEvent<HttpSession> sessionDestroyedEvent;
    private final ServletApiAbstraction servletApi;
    private final ServletContextService servletContextService;
    private final Container container;
    private static final ThreadLocal<Counter> nestedInvocationGuard = new ThreadLocal<HttpContextLifecycle.Counter>();
    private final boolean nestedInvocationGuardEnabled;
    private static class Counter {
        private int value = 1;
    }
    public HttpContextLifecycle(BeanManagerImpl beanManager, HttpContextActivationFilter contextActivationFilter, boolean ignoreForwards, boolean ignoreIncludes, boolean lazyConversationContext, boolean nestedInvocationGuardEnabled) {
        this.beanManager = beanManager;
        this.conversationContextActivator = new ConversationContextActivator(beanManager, lazyConversationContext);
        this.conversationActivationEnabled = null;
        this.ignoreForwards = ignoreForwards;
        this.ignoreIncludes = ignoreIncludes;
        this.contextActivationFilter = contextActivationFilter;
        this.applicationInitializedEvent = FastEvent.of(ServletContext.class, beanManager, InitializedLiteral.APPLICATION);
        this.applicationDestroyedEvent = FastEvent.of(ServletContext.class, beanManager, DestroyedLiteral.APPLICATION);
        this.requestInitializedEvent = FastEvent.of(HttpServletRequest.class, beanManager, InitializedLiteral.REQUEST);
        this.requestDestroyedEvent = FastEvent.of(HttpServletRequest.class, beanManager, DestroyedLiteral.REQUEST);
        this.sessionInitializedEvent = FastEvent.of(HttpSession.class, beanManager, InitializedLiteral.SESSION);
        this.sessionDestroyedEvent = FastEvent.of(HttpSession.class, beanManager, DestroyedLiteral.SESSION);
        this.servletApi = beanManager.getServices().get(ServletApiAbstraction.class);
        this.servletContextService = beanManager.getServices().get(ServletContextService.class);
        this.nestedInvocationGuardEnabled = nestedInvocationGuardEnabled;
        this.container = Container.instance(beanManager);
    }
    private HttpSessionDestructionContext getSessionDestructionContext() {
        if (sessionDestructionContextCache == null) {
            this.sessionDestructionContextCache = beanManager.instance().select(HttpSessionDestructionContext.class).get();
        }
        return sessionDestructionContextCache;
    }
    private HttpSessionContext getSessionContext() {
        if (sessionContextCache == null) {
            this.sessionContextCache = beanManager.instance().select(HttpSessionContext.class).get();
        }
        return sessionContextCache;
    }
    public HttpRequestContext getRequestContext() {
        if (requestContextCache == null) {
            this.requestContextCache = beanManager.instance().select(HttpRequestContext.class).get();
        }
        return requestContextCache;
    }
    public void contextInitialized(ServletContext ctx) {
        servletContextService.contextInitialized(ctx);
        synchronized (container) {
            applicationInitializedEvent.fire(ctx);
        }
    }
    public void contextDestroyed(ServletContext ctx) {
        synchronized (container) {
            applicationDestroyedEvent.fire(ctx);
        }
    }
    public void sessionCreated(HttpSession session) {
        SessionHolder.sessionCreated(session);
        conversationContextActivator.sessionCreated(session);
        sessionInitializedEvent.fire(session);
    }
    public void sessionDestroyed(HttpSession session) {
        deactivateSessionDestructionContext(session);
        boolean destroyed = getSessionContext().destroy(session);
        SessionHolder.clear();
        RequestScopedCache.endRequest();
        if (destroyed) {
            sessionDestroyedEvent.fire(session);
        } else {
            if (getRequestContext() instanceof HttpRequestContextImpl) {
                HttpServletRequest request = Reflections.<HttpRequestContextImpl> cast(getRequestContext()).getHttpServletRequest();
                request.setAttribute(HTTP_SESSION, session);
            }
        }
    }
    private void deactivateSessionDestructionContext(HttpSession session) {
        HttpSessionDestructionContext context = getSessionDestructionContext();
        if (context.isActive()) {
            context.deactivate();
            context.dissociate(session);
        }
    }
    public void requestInitialized(HttpServletRequest request, ServletContext ctx) {
        if (nestedInvocationGuardEnabled) {
            Counter counter = nestedInvocationGuard.get();
            Object marker = request.getAttribute(GUARD_PARAMETER_NAME);
            if (counter != null && marker != null) {
                counter.value++;
                return;
            } else {
                if (counter != null && marker == null) {
                    ServletLogger.LOG.guardLeak(counter.value);
                }
                nestedInvocationGuard.set(new Counter());
                request.setAttribute(GUARD_PARAMETER_NAME, GUARD_PARAMETER_VALUE);
            }
        }
        if (ignoreForwards && isForwardedRequest(request)) {
            return;
        }
        if (ignoreIncludes && isIncludedRequest(request)) {
            return;
        }
        if (!contextActivationFilter.accepts(request)) {
            return;
        }
        ServletLogger.LOG.requestInitialized(request);
        SessionHolder.requestInitialized(request);
        getRequestContext().associate(request);
        getSessionContext().associate(request);
        if (conversationActivationEnabled) {
            conversationContextActivator.associateConversationContext(request);
        }
        getRequestContext().activate();
        getSessionContext().activate();
        try {
            if (conversationActivationEnabled) {
                conversationContextActivator.activateConversationContext(request);
            }
            requestInitializedEvent.fire(request);
        } catch (RuntimeException e) {
            try {
                requestDestroyed(request);
            } catch (Exception ignored) {
            }
            request.setAttribute(REQUEST_DESTROYED, Boolean.TRUE);
            throw e;
        }
    }
    public void requestDestroyed(HttpServletRequest request) {
        if (isRequestDestroyed(request)) {
            return;
        }
        if (nestedInvocationGuardEnabled) {
            Counter counter = nestedInvocationGuard.get();
            if (counter != null) {
                counter.value--;
                if (counter.value > 0) {
                    return; 
                } else {
                    nestedInvocationGuard.remove(); 
                    request.removeAttribute(GUARD_PARAMETER_NAME);
                }
            } else {
                ServletLogger.LOG.guardNotSet();
                return;
            }
        }
        if (ignoreForwards && isForwardedRequest(request)) {
            return;
        }
        if (ignoreIncludes && isIncludedRequest(request)) {
            return;
        }
        if (!contextActivationFilter.accepts(request)) {
            return;
        }
        ServletLogger.LOG.requestDestroyed(request);
        try {
            conversationContextActivator.deactivateConversationContext(request);
            if (!servletApi.isAsyncSupported() || !servletApi.isAsyncStarted(request)) {
                getRequestContext().invalidate();
            }
            getRequestContext().deactivate();
            requestDestroyedEvent.fire(request);
            getSessionContext().deactivate();
            if (!getSessionContext().isValid()) {
                sessionDestroyedEvent.fire((HttpSession) request.getAttribute(HTTP_SESSION));
            }
        } finally {
            getRequestContext().dissociate(request);
            try {
                getSessionContext().dissociate(request);
            } catch (Exception e) {
                ServletLogger.LOG.unableToDissociateContext(getSessionContext(), request);
                ServletLogger.LOG.catchingDebug(e);
            }
            conversationContextActivator.disassociateConversationContext(request);
            SessionHolder.clear();
        }
    }
    public boolean isConversationActivationSet() {
        return conversationActivationEnabled != null;
    }
    public void setConversationActivationEnabled(boolean conversationActivationEnabled) {
        this.conversationActivationEnabled = conversationActivationEnabled;
    }
    private boolean isIncludedRequest(HttpServletRequest request) {
        return request.getAttribute(INCLUDE_HEADER) != null;
    }
    private boolean isForwardedRequest(HttpServletRequest request) {
        return request.getAttribute(FORWARD_HEADER) != null;
    }
    private boolean isRequestDestroyed(HttpServletRequest request) {
        return request.getAttribute(REQUEST_DESTROYED) != null;
    }
    @Override
    public void cleanup() {
    }
}
