
package org.jboss.weld.logging;
import static org.jboss.weld.logging.WeldLogger.WELD_PROJECT_CODE;
import javax.enterprise.context.spi.Context;
import javax.servlet.http.HttpServletRequest;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.Message.Format;
import org.jboss.logging.annotations.MessageLogger;
import org.jboss.weld.exceptions.IllegalStateException;
import org.jboss.weld.servlet.ServletContextService;
@MessageLogger(projectCode = WELD_PROJECT_CODE)
public interface ServletLogger extends WeldLogger {
    ServletLogger LOG = Logger.getMessageLogger(ServletLogger.class, Category.SERVLET.getName());
    @Message(id = 707, value = "Non Http-Servlet lifecycle not defined")
    IllegalStateException onlyHttpServletLifecycleDefined();
    @LogMessage(level = Level.TRACE)
    @Message(id = 708, value = "Initializing request {0}", format = Format.MESSAGE_FORMAT)
    void requestInitialized(Object param1);
    @LogMessage(level = Level.TRACE)
    @Message(id = 709, value = "Destroying request {0}", format = Format.MESSAGE_FORMAT)
    void requestDestroyed(Object param1);
    @Message(id = 710, value = "Cannot inject {0} outside of a Servlet request", format = Format.MESSAGE_FORMAT)
    IllegalStateException cannotInjectObjectOutsideOfServletRequest(Object param1, @Cause Throwable cause);
    @LogMessage(level = Level.WARN)
    @Message(id = 711, value = "Context activation pattern {0} ignored as it is overriden by the integrator.", format = Format.MESSAGE_FORMAT)
    void webXmlMappingPatternIgnored(String pattern);
    @LogMessage(level = Level.WARN)
    @Message(id = 712, value = "Unable to dissociate context {0} when destroying request {1}", format = Format.MESSAGE_FORMAT)
    void unableToDissociateContext(Context context, HttpServletRequest request);
    @Message(id = 713, value = "Unable to inject ServletContext. None is associated with {0}, {1}", format = Format.MESSAGE_FORMAT)
    IllegalStateException cannotInjectServletContext(ClassLoader classLoader, ServletContextService service);
    @LogMessage(level = Level.WARN)
    @Message(id = 714, value = "HttpContextLifecycle guard leak detected. The Servlet container is not fully compliant. The value was {0}", format = Format.MESSAGE_FORMAT)
    void guardLeak(int value);
    @LogMessage(level = Level.WARN)
    @Message(id = 715, value = "HttpContextLifecycle guard not set. The Servlet container is not fully compliant.", format = Format.MESSAGE_FORMAT)
    void guardNotSet();
    @LogMessage(level = Level.INFO)
    @Message(id = 716, value = "Running in Servlet 2.x environment. Asynchronous request support is disabled.")
    void servlet2Environment();
}