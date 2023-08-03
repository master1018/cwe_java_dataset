
package hudson.security;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import hudson.Util;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
public class AuthenticationProcessingFilter2 extends AuthenticationProcessingFilter {
    @Override
    protected String determineTargetUrl(HttpServletRequest request) {
        String targetUrl = request.getParameter("from");
        request.getSession().setAttribute("from", targetUrl);
        if (targetUrl == null)
            return getDefaultTargetUrl();
        if (Util.isAbsoluteUri(targetUrl))
            return "."; 
        if(targetUrl.startsWith(request.getContextPath()))
            return targetUrl.substring(request.getContextPath().length());
        return targetUrl;
    }
    @Override
    protected String determineFailureUrl(HttpServletRequest request, AuthenticationException failed) {
        Properties excMap = getExceptionMappings();
		String failedClassName = failed.getClass().getName();
		String whereFrom = request.getParameter("from");
		request.getSession().setAttribute("from", whereFrom);
		return excMap.getProperty(failedClassName, getAuthenticationFailureUrl());
    }
    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request,response,authResult);
        request.getSession().invalidate();
        request.getSession();
    }
    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        super.onUnsuccessfulAuthentication(request, response, failed);
        LOGGER.log(Level.INFO, "Login attempt failed", failed);
    }
    private static final Logger LOGGER = Logger.getLogger(AuthenticationProcessingFilter2.class.getName());
}
