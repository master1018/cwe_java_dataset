package org.jolokia.restrictor;
import java.io.InputStream;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.jolokia.util.HttpMethod;
import org.jolokia.util.RequestType;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
public class PolicyBasedRestrictorTest {
    @Test
    public void basics() throws MalformedObjectNameException {
        InputStream is = getClass().getResourceAsStream("/access-sample1.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"Verbose"));
        assertFalse(restrictor.isAttributeWriteAllowed(new ObjectName("java.lang:type=Memory"),"Verbose"));
        assertFalse(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"NonHeapMemoryUsage"));
        assertTrue(restrictor.isOperationAllowed(new ObjectName("java.lang:type=Memory"),"gc"));
        assertFalse(restrictor.isOperationAllowed(new ObjectName("java.lang:type=Threading"),"gc"));
        assertTrue(restrictor.isHttpMethodAllowed(HttpMethod.POST));
        assertFalse(restrictor.isHttpMethodAllowed(HttpMethod.GET));
    }
    @Test
    public void restrictIp() {
        InputStream is = getClass().getResourceAsStream("/access-sample1.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        String ips[][] = {
                { "11.0.18.32", "true" },
                { "planck", "true" },
                { "heisenberg", "false" },
                { "10.0.11.125", "true" },
                { "10.0.11.126", "false" },
                { "11.1.18.32", "false" },
                { "192.168.15.3", "true" },
                { "192.168.15.8", "true" },
                { "192.168.16.3", "false" }
        };
        for (String check[] : ips) {
            String res = restrictor.isRemoteAccessAllowed(check[0]) ? "true" : "false";
            assertEquals("Ip " + check[0] + " is " +
                         (check[1].equals("false") ? "not " : "") +
                         "allowed",check[1],res);
        }
    }
    @Test
    public void patterns() throws MalformedObjectNameException {
        InputStream is = getClass().getResourceAsStream("/access-sample2.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"HeapMemoryUsage"));
        assertFalse(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"NonHeapMemoryUsage"));
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("jolokia:type=Config,name=Bla"),"Debug"));
        assertFalse(restrictor.isOperationAllowed(new ObjectName("jolokia:type=Threading"),"gc"));
        assertTrue(restrictor.isRemoteAccessAllowed("10.0.1.125"));
    }
    @Test
    public void noRestrictions() throws MalformedObjectNameException {
        InputStream is = getClass().getResourceAsStream("/access-sample3.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"HeapMemoryUsage"));
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"NonHeapMemoryUsage"));
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("jolokia:type=Config,name=Bla"),"Debug"));
        assertTrue(restrictor.isOperationAllowed(new ObjectName("jolokia:type=Threading"),"gc"));
        assertTrue(restrictor.isTypeAllowed(RequestType.READ));
        assertTrue(restrictor.isHttpMethodAllowed(HttpMethod.GET));
        assertTrue(restrictor.isHttpMethodAllowed(HttpMethod.POST));
    }
    @Test
    public void deny() throws MalformedObjectNameException {
        InputStream is = getClass().getResourceAsStream("/access-sample4.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertFalse(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"HeapMemoryUsage"));
        assertFalse(restrictor.isAttributeWriteAllowed(new ObjectName("java.lang:type=Memory"),"HeapMemoryUsage"));
        assertFalse(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"NonHeapMemoryUsage"));
        assertTrue(restrictor.isAttributeWriteAllowed(new ObjectName("java.lang:type=Memory"),"NonHeapMemoryUsage"));
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"),"BlaUsage"));
        assertFalse(restrictor.isAttributeReadAllowed(new ObjectName("jolokia:type=Config"),"Debug"));
        assertFalse(restrictor.isOperationAllowed(new ObjectName("java.lang:type=Blubber,name=x"),"gc"));
        assertTrue(restrictor.isOperationAllowed(new ObjectName("java.lang:type=Blubber,name=x"),"xavier"));
    }
    @Test
    public void allow() throws MalformedObjectNameException {
        InputStream is = getClass().getResourceAsStream("/access-sample5.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));
        assertTrue(restrictor.isAttributeWriteAllowed(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage"));
        assertFalse(restrictor.isAttributeWriteAllowed(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage"));
        assertFalse(restrictor.isAttributeReadAllowed(new ObjectName("java.lang:type=Memory"), "BlaUsage"));
        assertTrue(restrictor.isAttributeReadAllowed(new ObjectName("jolokia:type=Config"), "Debug"));
        assertTrue(restrictor.isOperationAllowed(new ObjectName("java.lang:type=Blubber,name=x"), "gc"));
        assertFalse(restrictor.isOperationAllowed(new ObjectName("java.lang:type=Blubber,name=x"), "xavier"));
    }
    @Test
    public void illegalXml() {
        InputStream is = getClass().getResourceAsStream("/illegal1.xml");
        try {
            PolicyRestrictor restrictor = new PolicyRestrictor(is);
            fail("Could parse illegal file");
        } catch (SecurityException exp) {
        }
        try {
            new PolicyRestrictor(null);
            fail("No file given");
        } catch (SecurityException exp) {
        }
    }
    @Test
    public void noName() {
        InputStream is = getClass().getResourceAsStream("/illegal2.xml");
        try {
            PolicyRestrictor restrictor = new PolicyRestrictor(is);
            fail("Could parse illegal file");
        } catch (SecurityException exp) {
            assertTrue(exp.getMessage().contains("name"));
        }
    }
    @Test
    public void invalidTag() {
        InputStream is = getClass().getResourceAsStream("/illegal3.xml");
        try {
            PolicyRestrictor restrictor = new PolicyRestrictor(is);
            fail("Could parse illegal file");
        } catch (SecurityException exp) {
            assertTrue(exp.getMessage().contains("name"));
            assertTrue(exp.getMessage().contains("attribute"));
            assertTrue(exp.getMessage().contains("operation"));
            assertTrue(exp.getMessage().contains("bla"));
        }
    }
    @Test
    public void doubleName() {
        InputStream is = getClass().getResourceAsStream("/illegal4.xml");
        try {
            PolicyRestrictor restrictor = new PolicyRestrictor(is);
            fail("Could parse illegal file");
        } catch (SecurityException exp) {
            assertTrue(exp.getMessage().contains("name"));
        }
    }
    @Test
    public void httpMethod() {
        InputStream is = getClass().getResourceAsStream("/method.xml");
        PolicyRestrictor res = new PolicyRestrictor(is);
        assertTrue(res.isHttpMethodAllowed(HttpMethod.GET));
        assertTrue(res.isHttpMethodAllowed(HttpMethod.POST));
    }
    @Test
    public void illegalHttpMethod() {
        InputStream is = getClass().getResourceAsStream("/illegal5.xml");
        try {
            new PolicyRestrictor(is);
            fail();
        } catch (SecurityException exp) {
            assertTrue(exp.getMessage().contains("BLA"));
        }
    }
    @Test
    public void illegalHttpMethodTag() {
        InputStream is = getClass().getResourceAsStream("/illegal6.xml");
        try {
            new PolicyRestrictor(is);
            fail();
        } catch (SecurityException exp) {
            assertTrue(exp.getMessage().contains("method"));
            assertTrue(exp.getMessage().contains("blubber"));
        }
    }
    @Test
    public void cors() {
        InputStream is = getClass().getResourceAsStream("/allow-origin1.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertFalse(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("https:
    }
    @Test
    public void corsWildCard() {
        InputStream is = getClass().getResourceAsStream("/allow-origin2.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("http:
    }
    @Test
    public void corsEmpty() {
        InputStream is = getClass().getResourceAsStream("/allow-origin3.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("http:
    }
    @Test
    public void corsNoTags() {
        InputStream is = getClass().getResourceAsStream("/access-sample1.xml");
        PolicyRestrictor restrictor = new PolicyRestrictor(is);
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("http:
        assertTrue(restrictor.isCorsAccessAllowed("https:
    }
}
