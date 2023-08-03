
package org.apache.cxf.systest.https.hostname;
import java.net.URL;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.testutil.common.AbstractBusTestServerBase;
public class HostnameVerificationDeprecatedServer extends AbstractBusTestServerBase {
    public HostnameVerificationDeprecatedServer() {
    }
    protected void run()  {
        URL busFile = HostnameVerificationDeprecatedServer.class.getResource("hostname-server-bethal.xml");
        Bus busLocal = new SpringBusFactory().createBus(busFile);
        BusFactory.setDefaultBus(busLocal);
        setBus(busLocal);
        try {
            new HostnameVerificationDeprecatedServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
