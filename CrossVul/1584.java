
package org.jboss.weld.servlet;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jboss.weld.context.AbstractConversationContext;
import org.jboss.weld.context.ConversationContext;
import org.jboss.weld.context.http.HttpConversationContext;
import org.jboss.weld.context.http.LazyHttpConversationContextImpl;
import org.jboss.weld.event.FastEvent;
import org.jboss.weld.literal.DestroyedLiteral;
import org.jboss.weld.literal.InitializedLiteral;
import org.jboss.weld.logging.ConversationLogger;
import org.jboss.weld.logging.ServletLogger;
import org.jboss.weld.manager.BeanManagerImpl;
public class ConversationContextActivator {
    private static final String NO_CID = "nocid";
    private static final String CONVERSATION_PROPAGATION = "conversationPropagation";
    private static final String CONVERSATION_PROPAGATION_NONE = "none";
    private static final String CONTEXT_ACTIVATED_IN_REQUEST = ConversationContextActivator.class.getName() + "CONTEXT_ACTIVATED_IN_REQUEST";
    private final BeanManagerImpl beanManager;
    private HttpConversationContext httpConversationContextCache;
    private final FastEvent<HttpServletRequest> conversationInitializedEvent;
    private final FastEvent<HttpServletRequest> conversationDestroyedEvent;
    private final Consumer<HttpServletRequest> lazyInitializationCallback;
    private final boolean lazy;
    protected ConversationContextActivator(BeanManagerImpl beanManager, boolean lazy) {
        this.beanManager = beanManager;
        conversationInitializedEvent = FastEvent.of(HttpServletRequest.class, beanManager, InitializedLiteral.CONVERSATION);
        conversationDestroyedEvent = FastEvent.of(HttpServletRequest.class, beanManager, DestroyedLiteral.CONVERSATION);
        lazyInitializationCallback = lazy ? conversationInitializedEvent::fire : null;
        this.lazy = lazy;
    }
    private HttpConversationContext httpConversationContext() {
        if (httpConversationContextCache == null) {
            this.httpConversationContextCache = beanManager.instance().select(HttpConversationContext.class).get();
        }
        return httpConversationContextCache;
    }
    public void startConversationContext(HttpServletRequest request) {
        associateConversationContext(request);
        activateConversationContext(request);
    }
    public void stopConversationContext(HttpServletRequest request) {
        deactivateConversationContext(request);
    }
    protected void activateConversationContext(HttpServletRequest request) {
        HttpConversationContext conversationContext = httpConversationContext();
        if (!isContextActivatedInRequest(request)) {
            setContextActivatedInRequest(request);
            activate(conversationContext, request);
        } else {
            conversationContext.dissociate(request);
            conversationContext.associate(request);
            activate(conversationContext, request);
        }
    }
    private void activate(HttpConversationContext conversationContext, final HttpServletRequest request) {
        if (lazy && conversationContext instanceof LazyHttpConversationContextImpl) {
            LazyHttpConversationContextImpl lazyConversationContext = (LazyHttpConversationContextImpl) conversationContext;
            lazyConversationContext.activate(lazyInitializationCallback);
        } else {
            String cid = determineConversationId(request, conversationContext.getParameterName());
            conversationContext.activate(cid);
            if (cid == null) { 
                conversationInitializedEvent.fire(request);
            }
        }
    }
    protected void associateConversationContext(HttpServletRequest request) {
        httpConversationContext().associate(request);
    }
    private void setContextActivatedInRequest(HttpServletRequest request) {
        request.setAttribute(CONTEXT_ACTIVATED_IN_REQUEST, true);
    }
    private boolean isContextActivatedInRequest(HttpServletRequest request) {
        Object result = request.getAttribute(CONTEXT_ACTIVATED_IN_REQUEST);
        if (result == null) {
            return false;
        }
        return (Boolean) result;
    }
    protected void deactivateConversationContext(HttpServletRequest request) {
        ConversationContext conversationContext = httpConversationContext();
        if (conversationContext.isActive()) {
            if (conversationContext instanceof LazyHttpConversationContextImpl) {
                LazyHttpConversationContextImpl lazyConversationContext = (LazyHttpConversationContextImpl) conversationContext;
                if (!lazyConversationContext.isInitialized()) {
                    lazyConversationContext.deactivate();
                    return;
                }
            }
            boolean isTransient = conversationContext.getCurrentConversation().isTransient();
            if (ConversationLogger.LOG.isTraceEnabled()) {
                if (isTransient) {
                    ConversationLogger.LOG.cleaningUpTransientConversation();
                } else {
                    ConversationLogger.LOG.cleaningUpConversation(conversationContext.getCurrentConversation().getId());
                }
            }
            conversationContext.invalidate();
            conversationContext.deactivate();
            if (isTransient) {
                conversationDestroyedEvent.fire(request);
            }
        }
    }
    protected void disassociateConversationContext(HttpServletRequest request) {
        try {
            httpConversationContext().dissociate(request);
        } catch (Exception e) {
            ServletLogger.LOG.unableToDissociateContext(httpConversationContext(), request);
            ServletLogger.LOG.catchingDebug(e);
        }
    }
    public void sessionCreated(HttpSession session) {
        HttpConversationContext httpConversationContext = httpConversationContext();
        if (httpConversationContext instanceof AbstractConversationContext) {
            AbstractConversationContext<?, ?> abstractConversationContext = (AbstractConversationContext<?, ?>) httpConversationContext;
            abstractConversationContext.sessionCreated();
        }
    }
    public static String determineConversationId(HttpServletRequest request, String parameterName) {
        if (request == null) {
            throw ConversationLogger.LOG.mustCallAssociateBeforeActivate();
        }
        if (request.getParameter(NO_CID) != null) {
            return null; 
        }
        if (CONVERSATION_PROPAGATION_NONE.equals(request.getParameter(CONVERSATION_PROPAGATION))) {
            return null; 
        }
        String cidName = parameterName;
        String cid = request.getParameter(cidName);
        ConversationLogger.LOG.foundConversationFromRequest(cid);
        return cid;
    }
}
