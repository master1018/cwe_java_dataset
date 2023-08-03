package org.jolokia.backend;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.management.*;
import org.jolokia.backend.executor.NotChangedException;
import org.jolokia.config.ConfigKey;
import org.jolokia.config.Configuration;
import org.jolokia.converter.Converters;
import org.jolokia.converter.json.JsonConvertOptions;
import org.jolokia.detector.ServerHandle;
import org.jolokia.discovery.AgentDetails;
import org.jolokia.discovery.AgentDetailsHolder;
import org.jolokia.history.HistoryStore;
import org.jolokia.request.JmxRequest;
import org.jolokia.restrictor.AllowAllRestrictor;
import org.jolokia.restrictor.Restrictor;
import org.jolokia.util.*;
import org.json.simple.JSONObject;
import static org.jolokia.config.ConfigKey.*;
public class BackendManager implements AgentDetailsHolder {
    private LocalRequestDispatcher localDispatcher;
    private Converters converters;
    private JsonConvertOptions.Builder convertOptionsBuilder;
    private Restrictor restrictor;
    private HistoryStore historyStore;
    private DebugStore debugStore;
    private LogHandler logHandler;
    private List<RequestDispatcher> requestDispatchers;
    private volatile Initializer initializer;
    private AgentDetails agentDetails;
    public BackendManager(Configuration pConfig, LogHandler pLogHandler) {
        this(pConfig, pLogHandler, null);
    }
    public BackendManager(Configuration pConfig, LogHandler pLogHandler, Restrictor pRestrictor) {
        this(pConfig,pLogHandler,pRestrictor,false);
    }
    public BackendManager(Configuration pConfig, LogHandler pLogHandler, Restrictor pRestrictor, boolean pLazy) {
        restrictor = pRestrictor != null ? pRestrictor : new AllowAllRestrictor();
        logHandler = pLogHandler;
        agentDetails = new AgentDetails(pConfig);
        if (pLazy) {
            initializer = new Initializer(pConfig);
        } else {
            init(pConfig);
            initializer = null;
        }
    }
    public JSONObject handleRequest(JmxRequest pJmxReq) throws InstanceNotFoundException, AttributeNotFoundException,
            ReflectionException, MBeanException, IOException {
        lazyInitIfNeeded();
        boolean debug = isDebug();
        long time = 0;
        if (debug) {
            time = System.currentTimeMillis();
        }
        JSONObject json;
        try {
            json = callRequestDispatcher(pJmxReq);
            historyStore.updateAndAdd(pJmxReq,json);
            json.put("status",200 );
        } catch (NotChangedException exp) {
            json = new JSONObject();
            json.put("request",pJmxReq.toJSON());
            json.put("status",304);
            json.put("timestamp",System.currentTimeMillis() / 1000);
        }
        if (debug) {
            debug("Execution time: " + (System.currentTimeMillis() - time) + " ms");
            debug("Response: " + json);
        }
        return json;
    }
    public Object convertExceptionToJson(Throwable pExp, JmxRequest pJmxReq)  {
        JsonConvertOptions opts = getJsonConvertOptions(pJmxReq);
        try {
            JSONObject expObj =
                    (JSONObject) converters.getToJsonConverter().convertToJson(pExp,null,opts);
            return expObj;
        } catch (AttributeNotFoundException e) {
            return null;
        }
    }
    public void destroy() {
        try {
            localDispatcher.destroy();
        } catch (JMException e) {
            error("Cannot unregister MBean: " + e,e);
        }
    }
    public boolean isRemoteAccessAllowed(String pRemoteHost, String pRemoteAddr) {
        return restrictor.isRemoteAccessAllowed(pRemoteHost, pRemoteAddr);
    }
    public boolean isOriginAllowed(String pOrigin,boolean pStrictChecking) {
        return restrictor.isOriginAllowed(pOrigin, pStrictChecking);
    }
    public void info(String msg) {
        logHandler.info(msg);
        if (debugStore != null) {
            debugStore.log(msg);
        }
    }
    public void debug(String msg) {
        logHandler.debug(msg);
        if (debugStore != null) {
            debugStore.log(msg);
        }
    }
    public void error(String message, Throwable t) {
        logHandler.error(message, t);
        if (debugStore != null) {
            debugStore.log(message, t);
        }
    }
    public boolean isDebug() {
        return debugStore != null && debugStore.isDebug();
    }
    public AgentDetails getAgentDetails() {
        return agentDetails;
    }
    private final class Initializer {
        private Configuration config;
        private Initializer(Configuration pConfig) {
            config = pConfig;
        }
        void init() {
            BackendManager.this.init(config);
        }
    }
    private void lazyInitIfNeeded() {
        if (initializer != null) {
            synchronized (this) {
                if (initializer != null) {
                    initializer.init();
                    initializer = null;
                }
            }
        }
    }
    private void init(Configuration pConfig) {
        converters = new Converters();
        initLimits(pConfig);
        localDispatcher = new LocalRequestDispatcher(converters,
                                                     restrictor,
                                                     pConfig,
                                                     logHandler);
        ServerHandle serverHandle = localDispatcher.getServerHandle();
        requestDispatchers = createRequestDispatchers(pConfig.get(DISPATCHER_CLASSES),
                                                      converters,serverHandle,restrictor);
        requestDispatchers.add(localDispatcher);
        initMBeans(pConfig);
        agentDetails.setServerInfo(serverHandle.getVendor(),serverHandle.getProduct(),serverHandle.getVersion());
    }
    private void initLimits(Configuration pConfig) {
        if (pConfig != null) {
            convertOptionsBuilder = new JsonConvertOptions.Builder(
                    getNullSaveIntLimit(pConfig.get(MAX_DEPTH)),
                    getNullSaveIntLimit(pConfig.get(MAX_COLLECTION_SIZE)),
                    getNullSaveIntLimit(pConfig.get(MAX_OBJECTS))
            );
        } else {
            convertOptionsBuilder = new JsonConvertOptions.Builder();
        }
    }
    private int getNullSaveIntLimit(String pValue) {
        return pValue != null ? Integer.parseInt(pValue) : 0;
    }
    private List<RequestDispatcher> createRequestDispatchers(String pClasses,
                                                             Converters pConverters,
                                                             ServerHandle pServerHandle,
                                                             Restrictor pRestrictor) {
        List<RequestDispatcher> ret = new ArrayList<RequestDispatcher>();
        if (pClasses != null && pClasses.length() > 0) {
            String[] names = pClasses.split("\\s*,\\s*");
            for (String name : names) {
                ret.add(createDispatcher(name, pConverters, pServerHandle, pRestrictor));
            }
        }
        return ret;
    }
    private RequestDispatcher createDispatcher(String pDispatcherClass,
                                               Converters pConverters,
                                               ServerHandle pServerHandle, Restrictor pRestrictor) {
        try {
            Class clazz = this.getClass().getClassLoader().loadClass(pDispatcherClass);
            Constructor constructor = clazz.getConstructor(Converters.class,
                                                           ServerHandle.class,
                                                           Restrictor.class);
            return (RequestDispatcher)
                    constructor.newInstance(pConverters,
                                            pServerHandle,
                                            pRestrictor);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Couldn't load class " + pDispatcherClass + ": " + e,e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + pDispatcherClass + " has invalid constructor: " + e,e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Constructor of " + pDispatcherClass + " couldn't be accessed: " + e,e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(pDispatcherClass + " couldn't be instantiated: " + e,e);
        }
    }
    private JSONObject callRequestDispatcher(JmxRequest pJmxReq)
            throws InstanceNotFoundException, AttributeNotFoundException, ReflectionException, MBeanException, IOException, NotChangedException {
        Object retValue = null;
        boolean useValueWithPath = false;
        boolean found = false;
        for (RequestDispatcher dispatcher : requestDispatchers) {
            if (dispatcher.canHandle(pJmxReq)) {
                retValue = dispatcher.dispatchRequest(pJmxReq);
                useValueWithPath = dispatcher.useReturnValueWithPath(pJmxReq);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalStateException("Internal error: No dispatcher found for handling " + pJmxReq);
        }
        JsonConvertOptions opts = getJsonConvertOptions(pJmxReq);
        Object jsonResult =
                converters.getToJsonConverter()
                          .convertToJson(retValue, useValueWithPath ? pJmxReq.getPathParts() : null, opts);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",jsonResult);
        jsonObject.put("request",pJmxReq.toJSON());
        return jsonObject;
    }
    private JsonConvertOptions getJsonConvertOptions(JmxRequest pJmxReq) {
        return convertOptionsBuilder.
                    maxDepth(pJmxReq.getParameterAsInt(ConfigKey.MAX_DEPTH)).
                    maxCollectionSize(pJmxReq.getParameterAsInt(ConfigKey.MAX_COLLECTION_SIZE)).
                    maxObjects(pJmxReq.getParameterAsInt(ConfigKey.MAX_OBJECTS)).
                    faultHandler(pJmxReq.getValueFaultHandler()).
                    build();
    }
    private void initMBeans(Configuration pConfig) {
        int maxEntries = pConfig.getAsInt(HISTORY_MAX_ENTRIES);
        int maxDebugEntries = pConfig.getAsInt(DEBUG_MAX_ENTRIES);
        historyStore = new HistoryStore(maxEntries);
        debugStore = new DebugStore(maxDebugEntries, pConfig.getAsBoolean(DEBUG));
        try {
            localDispatcher.initMBeans(historyStore, debugStore);
        } catch (NotCompliantMBeanException e) {
            intError("Error registering config MBean: " + e, e);
        } catch (MBeanRegistrationException e) {
            intError("Cannot register MBean: " + e, e);
        } catch (MalformedObjectNameException e) {
            intError("Invalid name for config MBean: " + e, e);
        }
    }
    private void intError(String message,Throwable t) {
        logHandler.error(message, t);
        debugStore.log(message, t);
    }
}
