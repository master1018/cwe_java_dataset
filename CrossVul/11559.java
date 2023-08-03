
package org.glassfish.soteria.mechanisms.jaspic;
import static java.lang.Boolean.TRUE;
import static org.glassfish.soteria.Utils.isEmpty;
import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.module.ServerAuthModule;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.glassfish.soteria.cdi.spi.CDIPerRequestInitializer;
public final class Jaspic {
	public static final String IS_AUTHENTICATION = "org.glassfish.soteria.security.message.request.authentication";
	public static final String IS_AUTHENTICATION_FROM_FILTER = "org.glassfish.soteria.security.message.request.authenticationFromFilter";
	public static final String IS_SECURE_RESPONSE = "org.glassfish.soteria.security.message.request.secureResponse";
	public static final String IS_REFRESH = "org.glassfish.soteria.security.message.request.isRefresh";
	public static final String DID_AUTHENTICATION = "org.glassfish.soteria.security.message.request.didAuthentication";
	public static final String AUTH_PARAMS = "org.glassfish.soteria.security.message.request.authParams";
	public static final String LOGGEDIN_USERNAME = "org.glassfish.soteria.security.message.loggedin.username";
	public static final String LOGGEDIN_ROLES = "org.glassfish.soteria.security.message.loggedin.roles";
	public static final String LAST_AUTH_STATUS = "org.glassfish.soteria.security.message.authStatus";
	public static final String CONTEXT_REGISTRATION_ID = "org.glassfish.soteria.security.message.registrationId";
	private static final String IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";
	private static final String REGISTER_SESSION = "javax.servlet.http.registerSession";
	private Jaspic() {}
	public static boolean authenticate(HttpServletRequest request, HttpServletResponse response, AuthenticationParameters authParameters) {
		try {
			request.setAttribute(IS_AUTHENTICATION, true);
			if (authParameters != null) {
				request.setAttribute(AUTH_PARAMS, authParameters);
			}
			return request.authenticate(response);
		} catch (ServletException | IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			request.removeAttribute(IS_AUTHENTICATION);
			if (authParameters != null) {
				request.removeAttribute(AUTH_PARAMS);
			}
		}
	}
	public static AuthenticationParameters getAuthParameters(HttpServletRequest request) {
		AuthenticationParameters authParameters = (AuthenticationParameters) request.getAttribute(AUTH_PARAMS);
		if (authParameters == null) {
			authParameters = new AuthenticationParameters();
		}
		return authParameters;
	}
	public static void logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.logout();
			request.getSession().invalidate();
		} catch (ServletException e) {
			throw new IllegalArgumentException(e);
		}
	}
	public static void cleanSubject(Subject subject) {
	    if (subject != null) {
	        AccessController.doPrivileged(new PrivilegedAction<Void>() {
	            public Void run() {
	                subject.getPrincipals().clear();
	                return null;
	            }
	        });
	    }
	}
	public static boolean isRegisterSession(MessageInfo messageInfo) {
		return Boolean.valueOf((String)messageInfo.getMap().get(REGISTER_SESSION));
	}
	public static boolean isProtectedResource(MessageInfo messageInfo) {
		return Boolean.valueOf((String) messageInfo.getMap().get(IS_MANDATORY));
	}
	@SuppressWarnings("unchecked")
	public static void setRegisterSession(MessageInfo messageInfo, String username, Set<String> roles) {
		messageInfo.getMap().put("javax.servlet.http.registerSession", TRUE.toString());
		HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
		request.setAttribute(LOGGEDIN_USERNAME, username);
		request.setAttribute(LOGGEDIN_ROLES, roles);
	}
	public static boolean isAuthenticationRequest(HttpServletRequest request) {
		return TRUE.equals(request.getAttribute(IS_AUTHENTICATION));
	}
    public static void notifyContainerAboutLogin(Subject clientSubject, CallbackHandler handler, Principal callerPrincipal, Set<String> groups) {
        handleCallbacks(clientSubject, handler, new CallerPrincipalCallback(clientSubject, callerPrincipal), groups);
    }
    public static void notifyContainerAboutLogin(Subject clientSubject, CallbackHandler handler, String callerPrincipalName, Set<String> groups) {
        handleCallbacks(clientSubject, handler, new CallerPrincipalCallback(clientSubject, callerPrincipalName), groups);
    }
	private static void handleCallbacks(Subject clientSubject, CallbackHandler handler, CallerPrincipalCallback callerPrincipalCallback, Set<String> groups) {
	    if (clientSubject == null) {
	        throw new IllegalArgumentException("Null clientSubject!");
	    }
	    if (handler == null) {
	        throw new IllegalArgumentException("Null callback handler!");
	    }
	    try {
	        if (groups == null || isEmpty(groups) ||
	                (callerPrincipalCallback.getPrincipal() == null && callerPrincipalCallback.getName() == null)) {
	            handler.handle(new Callback[] {
	                    callerPrincipalCallback });
	        } else {
	            handler.handle(new Callback[] {
	                    callerPrincipalCallback,
	                    new GroupPrincipalCallback(clientSubject, groups.toArray(new String[groups.size()])) });
	        }
	    } catch (IOException | UnsupportedCallbackException e) {
	        throw new IllegalStateException(e);
	    }
	}
	public static void setLastAuthenticationStatus(HttpServletRequest request, AuthenticationStatus status) {
        request.setAttribute(LAST_AUTH_STATUS, status);
    }
	public static AuthenticationStatus getLastAuthenticationStatus(HttpServletRequest request) {
		return (AuthenticationStatus) request.getAttribute(LAST_AUTH_STATUS);
	}
	public static AuthStatus fromAuthenticationStatus(AuthenticationStatus authenticationStatus) {
	    switch (authenticationStatus) {
	        case NOT_DONE: case SUCCESS:
	            return AuthStatus.SUCCESS;
	        case SEND_FAILURE:
	            return AuthStatus.SEND_FAILURE;
	        case SEND_CONTINUE:
	            return AuthStatus.SEND_CONTINUE;
	        default:
	            throw new IllegalStateException("Unhandled status:" + authenticationStatus.name());
	    }
	}
	public static void setDidAuthentication(HttpServletRequest request) {
		request.setAttribute(DID_AUTHENTICATION, TRUE);
	}
	public static String getAppContextID(ServletContext context) {
		return context.getVirtualServerName() + " " + context.getContextPath();
	}
	public static String registerServerAuthModule(CDIPerRequestInitializer cdiPerRequestInitializer, ServletContext servletContext) {
	    String registrationId = AccessController.doPrivileged(new PrivilegedAction<String>() {
	        public String run() {
	            return AuthConfigFactory.getFactory().registerConfigProvider(
	                    new DefaultAuthConfigProvider(() -> new HttpBridgeServerAuthModule(cdiPerRequestInitializer)),
	                    "HttpServlet",
	                    getAppContextID(servletContext),
	                    "Default single SAM authentication config provider");
	        }
	    });
		servletContext.setAttribute(CONTEXT_REGISTRATION_ID, registrationId);
		return registrationId;
	}
	public static void deregisterServerAuthModule(ServletContext servletContext) {
		String registrationId = (String) servletContext.getAttribute(CONTEXT_REGISTRATION_ID);
		if (!isEmpty(registrationId)) {
			AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			    public Boolean run() {
			        return AuthConfigFactory.getFactory().removeRegistration(registrationId);
			    }
			});
		}
	}
}
