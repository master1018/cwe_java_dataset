
package org.apache.cxf.fediz.systests.idp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Base64;
import javax.servlet.ServletException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.fediz.core.FederationConstants;
import org.apache.cxf.fediz.core.util.DOMUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.wss4j.dom.engine.WSSConfig;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
public class IdpTest {
    static String idpHttpsPort;
    static String rpHttpsPort;
    private static Tomcat idpServer;
    @BeforeClass
    public static void init() throws Exception {
        idpHttpsPort = System.getProperty("idp.https.port");
        Assert.assertNotNull("Property 'idp.https.port' null", idpHttpsPort);
        rpHttpsPort = System.getProperty("rp.https.port");
        Assert.assertNotNull("Property 'rp.https.port' null", rpHttpsPort);
        idpServer = startServer(true, idpHttpsPort);
        WSSConfig.init();
    }
    private static Tomcat startServer(boolean idp, String port)
        throws ServletException, LifecycleException, IOException {
        Tomcat server = new Tomcat();
        server.setPort(0);
        String currentDir = new File(".").getCanonicalPath();
        String baseDir = currentDir + File.separator + "target";
        server.setBaseDir(baseDir);
        server.getHost().setAppBase("tomcat/idp/webapps");
        server.getHost().setAutoDeploy(true);
        server.getHost().setDeployOnStartup(true);
        Connector httpsConnector = new Connector();
        httpsConnector.setPort(Integer.parseInt(port));
        httpsConnector.setSecure(true);
        httpsConnector.setScheme("https");
        httpsConnector.setAttribute("keyAlias", "mytomidpkey");
        httpsConnector.setAttribute("keystorePass", "tompass");
        httpsConnector.setAttribute("keystoreFile", "test-classes/server.jks");
        httpsConnector.setAttribute("truststorePass", "tompass");
        httpsConnector.setAttribute("truststoreFile", "test-classes/server.jks");
        httpsConnector.setAttribute("clientAuth", "want");
        httpsConnector.setAttribute("sslProtocol", "TLS");
        httpsConnector.setAttribute("SSLEnabled", true);
        server.getService().addConnector(httpsConnector);
        File stsWebapp = new File(baseDir + File.separator + server.getHost().getAppBase(), "fediz-idp-sts");
        server.addWebapp("/fediz-idp-sts", stsWebapp.getAbsolutePath());
        File idpWebapp = new File(baseDir + File.separator + server.getHost().getAppBase(), "fediz-idp");
        server.addWebapp("/fediz-idp", idpWebapp.getAbsolutePath());
        server.start();
        return server;
    }
    @AfterClass
    public static void cleanup() {
        shutdownServer(idpServer);
    }
    private static void shutdownServer(Tomcat server) {
        try {
            if (server != null && server.getServer() != null
                && server.getServer().getState() != LifecycleState.DESTROYED) {
                if (server.getServer().getState() != LifecycleState.STOPPED) {
                    server.stop();
                }
                server.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getIdpHttpsPort() {
        return idpHttpsPort;
    }
    public String getRpHttpsPort() {
        return rpHttpsPort;
    }
    public String getServletContextName() {
        return "fedizhelloworld";
    }
    @org.junit.Test
    public void testSuccessfulInvokeOnIdP() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        final HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        DomNodeList<DomElement> results = idpPage.getElementsByTagName("input");
        String wresult = null;
        for (DomElement result : results) {
            if ("wresult".equals(result.getAttributeNS(null, "name"))) {
                wresult = result.getAttributeNS(null, "value");
                break;
            }
        }
        Assert.assertNotNull(wresult);
        webClient.close();
    }
    @org.junit.Test
    public void testSuccessfulSSOInvokeOnIdP() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.addRequestHeader("Authorization", "Basic "
            + Base64.getEncoder().encodeToString((user + ":" + password).getBytes()));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        DomNodeList<DomElement> results = idpPage.getElementsByTagName("input");
        String wresult = null;
        for (DomElement result : results) {
            if ("wresult".equals(result.getAttributeNS(null, "name"))) {
                wresult = result.getAttributeNS(null, "value");
                break;
            }
        }
        Assert.assertNotNull(wresult);
        webClient.removeRequestHeader("Authorization");
        webClient.addRequestHeader("Authorization", "Basic "
            + Base64.getEncoder().encodeToString(("mallory" + ":" + password).getBytes()));
        webClient.getOptions().setJavaScriptEnabled(false);
        idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        results = idpPage.getElementsByTagName("input");
        wresult = null;
        for (DomElement result : results) {
            if ("wresult".equals(result.getAttributeNS(null, "name"))) {
                wresult = result.getAttributeNS(null, "value");
                break;
            }
        }
        Assert.assertNotNull(wresult);
        webClient.close();
    }
    @Test
    public void testIdPMetadata() throws Exception {
        String url = "https:
            + "/fediz-idp/FederationMetadata/2007-06/FederationMetadata.xml";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setSSLClientCertificate(
            this.getClass().getClassLoader().getResource("client.jks"), "storepass", "jks");
        final XmlPage rpPage = webClient.getPage(url);
        final String xmlContent = rpPage.asXml();
        Assert.assertTrue(xmlContent.startsWith("<md:EntityDescriptor"));
        Document doc = rpPage.getXmlDocument();
        doc.getDocumentElement().setIdAttributeNS(null, "ID", true);
        Node signatureNode =
            DOMUtils.getChild(doc.getDocumentElement(), "Signature");
        Assert.assertNotNull(signatureNode);
        XMLSignature signature = new XMLSignature((Element)signatureNode, "");
        KeyInfo ki = signature.getKeyInfo();
        Assert.assertNotNull(ki);
        Assert.assertNotNull(ki.getX509Certificate());
        Assert.assertTrue(signature.checkSignatureValue(ki.getX509Certificate()));
        webClient.close();
    }
    @Test
    public void testIdPMetadataDefault() throws Exception {
        String url = "https:
            + "/fediz-idp/metadata";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setSSLClientCertificate(
            this.getClass().getClassLoader().getResource("client.jks"), "storepass", "jks");
        final XmlPage rpPage = webClient.getPage(url);
        final String xmlContent = rpPage.asXml();
        Assert.assertTrue(xmlContent.startsWith("<md:EntityDescriptor"));
        Document doc = rpPage.getXmlDocument();
        doc.getDocumentElement().setIdAttributeNS(null, "ID", true);
        Node signatureNode =
            DOMUtils.getChild(doc.getDocumentElement(), "Signature");
        Assert.assertNotNull(signatureNode);
        XMLSignature signature = new XMLSignature((Element)signatureNode, "");
        KeyInfo ki = signature.getKeyInfo();
        Assert.assertNotNull(ki);
        Assert.assertNotNull(ki.getX509Certificate());
        Assert.assertTrue(signature.checkSignatureValue(ki.getX509Certificate()));
        webClient.close();
    }
    @Test
    public void testIdPServiceMetadata() throws Exception {
        String url = "https:
            + "/fediz-idp/metadata/urn:org:apache:cxf:fediz:idp:realm-B";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setSSLClientCertificate(
            this.getClass().getClassLoader().getResource("client.jks"), "storepass", "jks");
        final XmlPage rpPage = webClient.getPage(url);
        final String xmlContent = rpPage.asXml();
        Assert.assertTrue(xmlContent.startsWith("<md:EntityDescriptor"));
        Document doc = rpPage.getXmlDocument();
        doc.getDocumentElement().setIdAttributeNS(null, "ID", true);
        Node signatureNode =
            DOMUtils.getChild(doc.getDocumentElement(), "Signature");
        Assert.assertNotNull(signatureNode);
        XMLSignature signature = new XMLSignature((Element)signatureNode, "");
        KeyInfo ki = signature.getKeyInfo();
        Assert.assertNotNull(ki);
        Assert.assertNotNull(ki.getX509Certificate());
        Assert.assertTrue(signature.checkSignatureValue(ki.getX509Certificate()));
        webClient.close();
    }
    @org.junit.Test
    public void testBadWReq() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String testWReq =
            "<RequestSecurityToken xmlns=\"http:
            + "<TokenType>http:
            + "</RequestSecurityToken>";
        url += "&wreq=" + URLEncoder.encode(testWReq, "UTF-8");
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreq value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testEntityExpansionWReq() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        InputStream is = this.getClass().getClassLoader().getResource("entity_wreq.xml").openStream();
        String entity = IOUtils.toString(is, "UTF-8");
        is.close();
        String validWreq =
            "<RequestSecurityToken xmlns=\"http:
            + "<TokenType>&m;http:
            + "</RequestSecurityToken>";
        url += "&wreq=" + URLEncoder.encode(entity + validWreq, "UTF-8");
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreq value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testMalformedWReq() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String testWReq =
            "<RequestSecurityToken xmlns=\"http:
            + "<TokenTypehttp:
            + "</RequestSecurityToken>";
        url += "&wreq=" + URLEncoder.encode(testWReq, "UTF-8");
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreq value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testBadWa() throws Exception {
        String url = "https:
        url += "wa=wsignin2.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wa value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testBadWHR() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A-xyz";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad whr value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 500);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testBadWtRealm() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld-xyz";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wtrealm value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testMalformedWReply() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "/localhost:" + getRpHttpsPort() + "/" + getServletContextName() + "/secure/fedservlet";
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testBadWReply() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
            + getServletContextName() + "/secure/fedservlet";
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testValidWReplyWrongApplication() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld2";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testWReplyExactMatchingSuccess() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld3";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getPage(url);
        webClient.close();
    }
    @org.junit.Test
    public void testWReplyExactMatchingFailure() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld3";
        String wreply = "https:
            + "/secure/fedservlet/blah";
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testNoEndpointAddressOrConstraint() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld4";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testWReplyWithDoubleSlashes() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
            + "/secure
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testLargeQueryParameterRejected() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        StringBuilder sb = new StringBuilder("https:
                + getServletContextName() + "/secure/fedservlet");
        for (int i = 0; i < 100; i++) {
            sb.append("aaaaaaaaaa");
        }
        url += "&wreply=" + sb.toString();
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        try {
            webClient.getPage(url);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @org.junit.Test
    public void testLargeQueryParameterAccepted() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        StringBuilder sb = new StringBuilder("https:
                + "/" + getServletContextName() + "/secure/fedservlet");
        for (int i = 0; i < 50; i++) {
            sb.append("aaaaaaaaaa");
        }
        url += "&wreply=" + sb.toString();
        String user = "alice";
        String password = "ecila";
        final WebClient webClient = new WebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getPage(url);
        webClient.close();
    }
    @Test
    public void testIdPLogout() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT;
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        idpPage = webClient.getPage(idpLogoutUrl);
        Assert.assertEquals("IDP SignOut Confirmation Response Page", idpPage.getTitleText());
        HtmlForm form = idpPage.getFormByName("signoutconfirmationresponseform");
        HtmlSubmitInput button = form.getInputByName("_eventId_submit");
        button.click();
        webClient.close();
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        idpPage = webClient.getPage(url);
        Assert.assertEquals(401, idpPage.getWebResponse().getStatusCode());
        webClient.close();
    }
    @Test
    public void testIdPLogoutCleanup() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT_CLEANUP;
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        idpPage = webClient.getPage(idpLogoutUrl);
        Assert.assertEquals("IDP SignOut Response Page", idpPage.getTitleText());
        webClient.close();
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        idpPage = webClient.getPage(url);
        Assert.assertEquals(401, idpPage.getWebResponse().getStatusCode());
        webClient.close();
    }
    @Test
    public void testIdPLogoutCleanupWithBadWReply() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String badWReply = "https:
            + "/secure
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT_CLEANUP;
        idpLogoutUrl += "&wreply=" + badWReply;
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        try {
            webClient.getPage(idpLogoutUrl);
            Assert.fail("Failure expected on a bad wreply value");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        idpPage = webClient.getPage(url);
        Assert.assertEquals(401, idpPage.getWebResponse().getStatusCode());
        webClient.close();
    }
    @Test
    public void testIdPLogoutWithWreplyConstraint() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String logoutWReply = "https:
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT + "&wreply=" + logoutWReply
            + "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        idpPage = webClient.getPage(idpLogoutUrl);
        Assert.assertEquals("IDP SignOut Confirmation Response Page", idpPage.getTitleText());
        HtmlForm form = idpPage.getFormByName("signoutconfirmationresponseform");
        HtmlSubmitInput button = form.getInputByName("_eventId_submit");
        button.click();
        webClient.close();
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        idpPage = webClient.getPage(url);
        Assert.assertEquals(401, idpPage.getWebResponse().getStatusCode());
        webClient.close();
    }
    @Test
    public void testIdPLogoutWithWreplyBadAddress() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String logoutWReply = "https:
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT + "&wreply=" + logoutWReply
            + "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        try {
            webClient.getPage(idpLogoutUrl);
            Assert.fail("Failure expected on a non-matching wreply address");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @Test
    public void testIdPLogoutWithNoRealm() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String logoutWReply = "https:
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT + "&wreply=" + logoutWReply;
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        try {
            webClient.getPage(idpLogoutUrl);
            Assert.fail("Failure expected on a non-matching wreply address");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @Test
    public void testIdPLogoutWithWreplyAddress() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld3";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String logoutWReply = "https:
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT + "&wreply=" + logoutWReply
            + "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld3";
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        idpPage = webClient.getPage(idpLogoutUrl);
        Assert.assertEquals("IDP SignOut Confirmation Response Page", idpPage.getTitleText());
        HtmlForm form = idpPage.getFormByName("signoutconfirmationresponseform");
        HtmlSubmitInput button = form.getInputByName("_eventId_submit");
        button.click();
        webClient.close();
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        idpPage = webClient.getPage(url);
        Assert.assertEquals(401, idpPage.getWebResponse().getStatusCode());
        webClient.close();
    }
    @Test
    public void testIdPLogoutWithBadAddress() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld3";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String logoutWReply = "https:
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT + "&wreply=" + logoutWReply
            + "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld3";
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        try {
            webClient.getPage(idpLogoutUrl);
            Assert.fail("Failure expected on a non-matching wreply address");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
    @Test
    public void testIdPLogoutWithNoConfiguredConstraint() throws Exception {
        String url = "https:
        url += "wa=wsignin1.0";
        url += "&whr=urn:org:apache:cxf:fediz:idp:realm-A";
        url += "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld2";
        String wreply = "https:
        url += "&wreply=" + wreply;
        String user = "alice";
        String password = "ecila";
        CookieManager cookieManager = new CookieManager();
        WebClient webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCredentialsProvider().setCredentials(
            new AuthScope("localhost", Integer.parseInt(getIdpHttpsPort())),
            new UsernamePasswordCredentials(user, password));
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage idpPage = webClient.getPage(url);
        webClient.getOptions().setJavaScriptEnabled(true);
        Assert.assertEquals("IDP SignIn Response Form", idpPage.getTitleText());
        webClient.close();
        String logoutWReply = "https:
        String idpLogoutUrl = "https:
            + FederationConstants.ACTION_SIGNOUT + "&wreply=" + logoutWReply
            + "&wtrealm=urn:org:apache:cxf:fediz:fedizhelloworld2";
        webClient = new WebClient();
        webClient.setCookieManager(cookieManager);
        webClient.getOptions().setUseInsecureSSL(true);
        try {
            webClient.getPage(idpLogoutUrl);
            Assert.fail("Failure expected on a non-matching wreply address");
        } catch (FailingHttpStatusCodeException ex) {
            Assert.assertEquals(ex.getStatusCode(), 400);
        }
        webClient.close();
    }
}
