
package org.glassfish.soteria.mechanisms.jaspic;
import java.util.Map;
import java.util.function.Supplier;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;
public class DefaultAuthConfigProvider implements AuthConfigProvider {
    private static final String CALLBACK_HANDLER_PROPERTY_NAME = "authconfigprovider.client.callbackhandler";
    private Map<String, String> providerProperties;
    private Supplier<ServerAuthModule> serverAuthModuleSupplier;
    public DefaultAuthConfigProvider(Supplier<ServerAuthModule> serverAuthModuleSupplier) {
        this.serverAuthModuleSupplier = serverAuthModuleSupplier;
    }
    public DefaultAuthConfigProvider(Map<String, String> properties, AuthConfigFactory factory) {
        this.providerProperties = properties;
        if (factory != null) {
            factory.registerConfigProvider(this, null, null, "Auto registration");
        }
    }
    @Override
    public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException,
        SecurityException {
        return new DefaultServerAuthConfig(layer, appContext, handler == null ? createDefaultCallbackHandler() : handler,
            providerProperties, serverAuthModuleSupplier);
    }
    @Override
    public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException,
        SecurityException {
        return null;
    }
    @Override
    public void refresh() {
    }
    private CallbackHandler createDefaultCallbackHandler() throws AuthException {
        String callBackClassName = System.getProperty(CALLBACK_HANDLER_PROPERTY_NAME);
        if (callBackClassName == null) {
            throw new AuthException("No default handler set via system property: " + CALLBACK_HANDLER_PROPERTY_NAME);
        }
        try {
            return (CallbackHandler) Thread.currentThread().getContextClassLoader().loadClass(callBackClassName).newInstance();
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }
}
