
package org.glassfish.soteria.mechanisms.jaspic;
import java.util.Collections;
import java.util.function.Supplier;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.ServerAuth;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;
public class DefaultServerAuthContext implements ServerAuthContext {
    private final ServerAuthModule serverAuthModule;
    public DefaultServerAuthContext(CallbackHandler handler, Supplier<ServerAuthModule> serverAuthModuleSupplier) throws AuthException {
        this.serverAuthModule = serverAuthModuleSupplier.get();
        serverAuthModule.initialize(null, null, handler, Collections.<String, String> emptyMap());
    }
    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
        throws AuthException {
        return serverAuthModule.validateRequest(messageInfo, clientSubject, serviceSubject);
    }
    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return serverAuthModule.secureResponse(messageInfo, serviceSubject);
    }
    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        serverAuthModule.cleanSubject(messageInfo, subject);
    }
}
