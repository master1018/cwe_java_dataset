
package org.glassfish.soteria.mechanisms.jaspic;
import java.util.Map;
import java.util.function.Supplier;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;
public class DefaultServerAuthConfig implements ServerAuthConfig {
    private String layer;
    private String appContext;
    private CallbackHandler handler;
    private Map<String, String> providerProperties;
    private Supplier<ServerAuthModule> serverAuthModuleSupplier;
    public DefaultServerAuthConfig(String layer, String appContext, CallbackHandler handler,
        Map<String, String> providerProperties, Supplier<ServerAuthModule> serverAuthModuleSupplier) {
        this.layer = layer;
        this.appContext = appContext;
        this.handler = handler;
        this.providerProperties = providerProperties;
        this.serverAuthModuleSupplier = serverAuthModuleSupplier;
    }
    @Override
    public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject,
        @SuppressWarnings("rawtypes") Map properties) throws AuthException {
        return new DefaultServerAuthContext(handler, serverAuthModuleSupplier);
    }
    @Override
    public String getMessageLayer() {
        return layer;
    }
    @Override
    public String getAuthContextID(MessageInfo messageInfo) {
        return appContext;
    }
    @Override
    public String getAppContext() {
        return appContext;
    }
    @Override
    public void refresh() {
    }
    @Override
    public boolean isProtected() {
        return false;
    }
    public Map<String, String> getProviderProperties() {
        return providerProperties;
    }
}
