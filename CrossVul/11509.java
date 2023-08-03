
package org.apache.cxf.transport.https;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
class AllowAllHostnameVerifier implements javax.net.ssl.HostnameVerifier {
    @Override
    public boolean verify(String host, SSLSession session) {
        try {
            Certificate[] certs = session.getPeerCertificates();
            return certs != null && certs[0] instanceof X509Certificate;
        } catch (SSLException e) {
            return false;
        }
    }
    public boolean verify(final String host, final String certHostname) {
        return certHostname != null && !certHostname.isEmpty();
    }
}