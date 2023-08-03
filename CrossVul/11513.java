
package org.apache.cxf.systest.https.hostname;
import java.net.URL;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.testutil.common.AbstractBusClientServerTestBase;
import org.apache.hello_world.Greeter;
import org.apache.hello_world.services.SOAPService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
public class HostnameVerificationDeprecatedTest extends AbstractBusClientServerTestBase {
    static final String PORT = allocatePort(HostnameVerificationDeprecatedServer.class);
    static final String PORT2 = allocatePort(HostnameVerificationDeprecatedServer.class, 2);
    @BeforeClass
    public static void startServers() throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        assertTrue(
            "Server failed to launch",
            launchServer(HostnameVerificationDeprecatedServer.class, true)
        );
    }
    @AfterClass
    public static void cleanup() throws Exception {
        System.clearProperty("java.protocol.handler.pkgs");
        stopAllServers();
    }
    @org.junit.Test
    public void testLocalhostNotMatching() throws Exception {
        SpringBusFactory bf = new SpringBusFactory();
        URL busFile = HostnameVerificationDeprecatedTest.class.getResource("hostname-client-bethal.xml");
        Bus bus = bf.createBus(busFile.toString());
        BusFactory.setDefaultBus(bus);
        BusFactory.setThreadDefaultBus(bus);
        URL url = SOAPService.WSDL_LOCATION;
        SOAPService service = new SOAPService(url, SOAPService.SERVICE);
        assertNotNull("Service is null", service);
        final Greeter port = service.getHttpsPort();
        assertNotNull("Port is null", port);
        updateAddressPort(port, PORT);
        try {
            port.greetMe("Kitty");
            fail("Failure expected on the hostname verification");
        } catch (Exception ex) {
        }
        ((java.io.Closeable)port).close();
        bus.shutdown(true);
    }
    @org.junit.Test
    public void testNoSubjectAlternativeNameCNMatch() throws Exception {
        SpringBusFactory bf = new SpringBusFactory();
        URL busFile = HostnameVerificationDeprecatedTest.class.getResource("hostname-client.xml");
        Bus bus = bf.createBus(busFile.toString());
        BusFactory.setDefaultBus(bus);
        BusFactory.setThreadDefaultBus(bus);
        URL url = SOAPService.WSDL_LOCATION;
        SOAPService service = new SOAPService(url, SOAPService.SERVICE);
        assertNotNull("Service is null", service);
        final Greeter port = service.getHttpsPort();
        assertNotNull("Port is null", port);
        updateAddressPort(port, PORT2);
        assertEquals(port.greetMe("Kitty"), "Hello Kitty");
        ((BindingProvider)port).getRequestContext().put("use.async.http.conduit", true);
        assertEquals(port.greetMe("Kitty"), "Hello Kitty");
        ((java.io.Closeable)port).close();
        bus.shutdown(true);
    }
}
