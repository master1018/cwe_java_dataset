
package org.apache.cxf.transport.https;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.ReflectionInvokationHandler;
import org.apache.cxf.common.util.ReflectionUtil;
import org.apache.cxf.configuration.jsse.SSLUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
public class HttpsURLConnectionFactory {
    public static final String HTTPS_URL_PROTOCOL_ID = "https";
    private static final Logger LOG =
        LogUtils.getL7dLogger(HttpsURLConnectionFactory.class);
    private static boolean weblogicWarned;
    SSLSocketFactory socketFactory;
    int lastTlsHash;
    public HttpsURLConnectionFactory() {
    }
    public HttpURLConnection createConnection(TLSClientParameters tlsClientParameters,
            Proxy proxy, URL url) throws IOException {
        HttpURLConnection connection =
            (HttpURLConnection) (proxy != null
                                   ? url.openConnection(proxy)
                                   : url.openConnection());
        if (HTTPS_URL_PROTOCOL_ID.equals(url.getProtocol())) {
            if (tlsClientParameters == null) {
                tlsClientParameters = new TLSClientParameters();
            }
            try {
                decorateWithTLS(tlsClientParameters, connection);
            } catch (Throwable ex) {
                if (ex instanceof IOException) {
                    throw (IOException) ex;
                }
                IOException ioException = new IOException("Error while initializing secure socket", ex);
                throw ioException;
            }
        }
        return connection;
    }
    protected synchronized void decorateWithTLS(TLSClientParameters tlsClientParameters,
            HttpURLConnection connection) throws GeneralSecurityException {
        int hash = tlsClientParameters.hashCode();
        if (hash != lastTlsHash) {
            lastTlsHash = hash;
            socketFactory = null;
        }
        if (tlsClientParameters.isUseHttpsURLConnectionDefaultSslSocketFactory()) {
            socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        } else if (tlsClientParameters.getSSLSocketFactory() != null) {
            socketFactory = tlsClientParameters.getSSLSocketFactory();
        } else if (socketFactory == null) {
            SSLContext ctx =
                org.apache.cxf.transport.https.SSLUtils.getSSLContext(tlsClientParameters);
            String[] cipherSuites =
                SSLUtils.getCiphersuitesToInclude(tlsClientParameters.getCipherSuites(),
                                                  tlsClientParameters.getCipherSuitesFilter(),
                                                  ctx.getSocketFactory().getDefaultCipherSuites(),
                                                  SSLUtils.getSupportedCipherSuites(ctx),
                                                  LOG);
            String protocol = tlsClientParameters.getSecureSocketProtocol() != null ? tlsClientParameters
                .getSecureSocketProtocol() : "TLS";
            socketFactory = new SSLSocketFactoryWrapper(ctx.getSocketFactory(), cipherSuites,
                                                        protocol);
            lastTlsHash = tlsClientParameters.hashCode();
        } else {
        }
        HostnameVerifier verifier = org.apache.cxf.transport.https.SSLUtils
            .getHostnameVerifier(tlsClientParameters);
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection conn = (HttpsURLConnection) connection;
            conn.setHostnameVerifier(verifier);
            conn.setSSLSocketFactory(socketFactory);
        } else {
            try {
                Method method = connection.getClass().getMethod("getHostnameVerifier");
                InvocationHandler handler = new ReflectionInvokationHandler(verifier) {
                    public Object invoke(Object proxy,
                                         Method method,
                                         Object[] args) throws Throwable {
                        try {
                            return super.invoke(proxy, method, args);
                        } catch (Exception ex) {
                            return false;
                        }
                    }
                };
                Object proxy = java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                        new Class[] {method.getReturnType()},
                                                                        handler);
                method = connection.getClass().getMethod("setHostnameVerifier", method.getReturnType());
                method.invoke(connection, proxy);
            } catch (Exception ex) {
            }
            try {
                Method getSSLSocketFactory = connection.getClass().getMethod("getSSLSocketFactory");
                Method setSSLSocketFactory = connection.getClass()
                    .getMethod("setSSLSocketFactory", getSSLSocketFactory.getReturnType());
                if (getSSLSocketFactory.getReturnType().isInstance(socketFactory)) {
                    setSSLSocketFactory.invoke(connection, socketFactory);
                } else {
                    Constructor<?> c = getSSLSocketFactory.getReturnType()
                        .getDeclaredConstructor(SSLSocketFactory.class);
                    ReflectionUtil.setAccessible(c);
                    setSSLSocketFactory.invoke(connection, c.newInstance(socketFactory));
                }
            } catch (Exception ex) {
                if (connection.getClass().getName().contains("weblogic")) {
                    if (!weblogicWarned) {
                        weblogicWarned = true;
                        LOG.warning("Could not configure SSLSocketFactory on Weblogic.  "
                                    + " Use the Weblogic control panel to configure the SSL settings.");
                    }
                    return;
                }
                throw new IllegalArgumentException("Error decorating connection class "
                        + connection.getClass().getName(), ex);
            }
        }
    }
    protected void addLogHandler(Handler handler) {
        LOG.addHandler(handler);
    }
}
