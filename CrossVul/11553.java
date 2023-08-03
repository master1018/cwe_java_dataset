
package org.richfaces.webapp;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.atmosphere.cpr.BroadcastFilter;
import org.atmosphere.cpr.Broadcaster.SCOPE;
import org.atmosphere.cpr.Meteor;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.Request;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.impl.RequestImpl;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
public class PushHandlerFilter implements Filter, Serializable {
    public static final String SESSION_ATTRIBUTE_NAME = Session.class.getName();
    public static final String REQUEST_ATTRIBUTE_NAME = Request.class.getName();
    private static final long serialVersionUID = 5724886106704391903L;
    public static final String PUSH_SESSION_ID_PARAM = "pushSessionId";
    private static final Logger LOGGER = RichfacesLogger.WEBAPP.getLogger();
    private int servletMajorVersion;
    private transient ServletContext servletContext;
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        servletMajorVersion = servletContext.getMajorVersion();
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            HttpServletResponse httpResp = (HttpServletResponse) response;
            if ("GET".equals(httpReq.getMethod())) {
                String pushSessionId = httpReq.getParameter(PUSH_SESSION_ID_PARAM);
                Session session = null;
                if (pushSessionId != null) {
                    ensureServletContextAvailable(request);
                    PushContext pushContext = (PushContext) servletContext.getAttribute(PushContext.INSTANCE_KEY_NAME);
                    session = pushContext.getSessionManager().getPushSession(pushSessionId);
                }
                if (session == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(MessageFormat.format("Session {0} was not found", pushSessionId));
                    }
                    httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                httpResp.setContentType("text/plain");
                Meteor meteor = Meteor.build(httpReq, SCOPE.REQUEST, Collections.<BroadcastFilter>emptyList(), null);
                try {
                    Request pushRequest = new RequestImpl(meteor, session);
                    httpReq.setAttribute(SESSION_ATTRIBUTE_NAME, session);
                    httpReq.setAttribute(REQUEST_ATTRIBUTE_NAME, pushRequest);
                    pushRequest.suspend();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
                return;
            }
        }
    }
    private void ensureServletContextAvailable(ServletRequest request) {
        if (servletContext == null) {
            if (servletMajorVersion >= 3) {
                servletContext = request.getServletContext();
            } else {
                throw new IllegalStateException(
                        "ServletContext is not available (you are using Servlets API <3.0; it might be caused by "
                                + PushHandlerFilter.class.getName() + " in distributed environment)");
            }
        }
    }
    public void destroy() {
        servletContext = null;
    }
}
