package org.jolokia.backend;
import java.io.IOException;
import java.util.Map;
import javax.management.*;
import org.jolokia.backend.executor.NotChangedException;
import org.jolokia.config.ConfigKey;
import org.jolokia.config.Configuration;
import org.jolokia.converter.Converters;
import org.jolokia.detector.ServerHandle;
import org.jolokia.request.JmxRequest;
import org.jolokia.request.JmxRequestBuilder;
import org.jolokia.restrictor.Restrictor;
import org.jolokia.util.*;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
public class BackendManagerTest {
    Configuration config;
    private LogHandler log = new LogHandler.StdoutLogHandler(true);
    @BeforeTest
    public void setup() {
        config = new Configuration(ConfigKey.AGENT_ID,"test");
    }
    @Test
    public void simpleRead() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        Configuration config = new Configuration(ConfigKey.DEBUG,"true",ConfigKey.AGENT_ID,"test");
        BackendManager backendManager = new BackendManager(config, log);
        JmxRequest req = new JmxRequestBuilder(RequestType.READ,"java.lang:type=Memory")
                .attribute("HeapMemoryUsage")
                .build();
        JSONObject ret = backendManager.handleRequest(req);
        assertTrue((Long) ((Map) ret.get("value")).get("used") > 0);
        backendManager.destroy();
    }
    @Test
    public void notChanged() throws MalformedObjectNameException, MBeanException, AttributeNotFoundException, ReflectionException, InstanceNotFoundException, IOException {
        Configuration config = new Configuration(ConfigKey.DISPATCHER_CLASSES,RequestDispatcherTest.class.getName(),ConfigKey.AGENT_ID,"test");
        BackendManager backendManager = new BackendManager(config, log);
        JmxRequest req = new JmxRequestBuilder(RequestType.LIST).build();
        JSONObject ret = backendManager.handleRequest(req);
        assertEquals(ret.get("status"),304);
        backendManager.destroy();
    }
    @Test
    public void lazyInit() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        BackendManager backendManager = new BackendManager(config, log, null, true  );
        JmxRequest req = new JmxRequestBuilder(RequestType.READ,"java.lang:type=Memory")
                .attribute("HeapMemoryUsage")
                .build();
        JSONObject ret = backendManager.handleRequest(req);
        assertTrue((Long) ((Map) ret.get("value")).get("used") > 0);
        backendManager.destroy();
    }
    @Test
    public void requestDispatcher() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        config = new Configuration(ConfigKey.DISPATCHER_CLASSES,RequestDispatcherTest.class.getName(),ConfigKey.AGENT_ID,"test");
        BackendManager backendManager = new BackendManager(config, log);
        JmxRequest req = new JmxRequestBuilder(RequestType.READ,"java.lang:type=Memory").build();
        backendManager.handleRequest(req);
        assertTrue(RequestDispatcherTest.called);
        backendManager.destroy();
    }
    @Test(expectedExceptions = IllegalArgumentException.class,expectedExceptionsMessageRegExp = ".*invalid constructor.*")
    public void requestDispatcherWithWrongDispatcher() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        Configuration config = new Configuration(ConfigKey.DISPATCHER_CLASSES,RequestDispatcherWrong.class.getName(),ConfigKey.AGENT_ID,"test");
        new BackendManager(config,log);
    }
    @Test(expectedExceptions = IllegalArgumentException.class,expectedExceptionsMessageRegExp = ".*blub.bla.Dispatcher.*")
    public void requestDispatcherWithUnkownDispatcher() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {
        Configuration config = new Configuration(ConfigKey.DISPATCHER_CLASSES,"blub.bla.Dispatcher",ConfigKey.AGENT_ID,"test");
        new BackendManager(config,log);
    }
    @Test
    public void debugging() {
        RecordingLogHandler lhandler = new RecordingLogHandler();
        BackendManager backendManager = new BackendManager(config,lhandler);
        lhandler.error = 0;
        lhandler.debug = 0;
        lhandler.info = 0;
        backendManager.debug("test");
        assertEquals(lhandler.debug,1);
        backendManager.error("test",new Exception());
        assertEquals(lhandler.error,1);
        backendManager.info("test");
        assertEquals(lhandler.info,1);
        backendManager.destroy();
    }
    @Test
    public void defaultConfig() {
        Configuration config = new Configuration(ConfigKey.DEBUG_MAX_ENTRIES,"blabal",ConfigKey.AGENT_ID,"test");
        BackendManager backendManager = new BackendManager(config,log);
        backendManager.destroy();
    }
    @Test
    public void doubleInit() {
        BackendManager b1 = new BackendManager(config,log);
        BackendManager b2 = new BackendManager(config,log);
        b2.destroy();
        b1.destroy();
    }
    @Test
    public void remoteAccessCheck() {
        BackendManager backendManager = new BackendManager(config,log);
        assertTrue(backendManager.isRemoteAccessAllowed("localhost","127.0.0.1"));
        backendManager.destroy();
    }
    @Test
    public void corsAccessCheck() {
        BackendManager backendManager = new BackendManager(config,log);
        assertTrue(backendManager.isOriginAllowed("http:
        backendManager.destroy();
    }
    @Test
    public void convertError() throws MalformedObjectNameException {
        BackendManager backendManager = new BackendManager(config,log);
        Exception exp = new IllegalArgumentException("Hans",new IllegalStateException("Kalb"));
        JmxRequest req = new JmxRequestBuilder(RequestType.READ,"java.lang:type=Memory").build();
        JSONObject jsonError = (JSONObject) backendManager.convertExceptionToJson(exp,req);
        assertTrue(!jsonError.containsKey("stackTrace"));
        assertEquals(jsonError.get("message"),"Hans");
        assertEquals(((JSONObject) jsonError.get("cause")).get("message"),"Kalb");
        backendManager.destroy();
    }
    static class RequestDispatcherTest implements RequestDispatcher {
        static boolean called = false;
        public RequestDispatcherTest(Converters pConverters,ServerHandle pServerHandle,Restrictor pRestrictor) {
            assertNotNull(pConverters);
            assertNotNull(pRestrictor);
        }
        public Object dispatchRequest(JmxRequest pJmxReq) throws InstanceNotFoundException, AttributeNotFoundException, ReflectionException, MBeanException, IOException, NotChangedException {
            called = true;
            if (pJmxReq.getType() == RequestType.READ) {
                return new JSONObject();
            } else if (pJmxReq.getType() == RequestType.WRITE) {
                return "faultyFormat";
            } else if (pJmxReq.getType() == RequestType.LIST) {
                throw new NotChangedException(pJmxReq);
            }
            return null;
        }
        public boolean canHandle(JmxRequest pJmxRequest) {
            return true;
        }
        public boolean useReturnValueWithPath(JmxRequest pJmxRequest) {
            return false;
        }
    }
    static class RequestDispatcherWrong implements RequestDispatcher {
        public Object dispatchRequest(JmxRequest pJmxReq) throws InstanceNotFoundException, AttributeNotFoundException, ReflectionException, MBeanException, IOException {
            return null;
        }
        public boolean canHandle(JmxRequest pJmxRequest) {
            return false;
        }
        public boolean useReturnValueWithPath(JmxRequest pJmxRequest) {
            return false;
        }
    }
    private class RecordingLogHandler implements LogHandler {
        int debug = 0;
        int info = 0;
        int error = 0;
        public void debug(String message) {
            debug++;
        }
        public void info(String message) {
            info++;
        }
        public void error(String message, Throwable t) {
            error++;
        }
    }
}
