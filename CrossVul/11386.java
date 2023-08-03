package org.jolokia.http;
import java.io.*;
import java.net.*;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;
import javax.management.RuntimeMBeanException;
import javax.security.auth.Subject;
import javax.servlet.*;
import javax.servlet.http.*;
import org.jolokia.backend.BackendManager;
import org.jolokia.config.*;
import org.jolokia.discovery.AgentDetails;
import org.jolokia.discovery.DiscoveryMulticastResponder;
import org.jolokia.restrictor.*;
import org.jolokia.util.*;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
public class AgentServlet extends HttpServlet {
    private static final long serialVersionUID = 42L;
    private ServletRequestHandler httpGetHandler, httpPostHandler;
    private BackendManager backendManager;
    private LogHandler logHandler;
    private HttpRequestHandler requestHandler;
    private Restrictor restrictor;
    private String configMimeType;
    private DiscoveryMulticastResponder discoveryMulticastResponder;
    private boolean allowDnsReverseLookup;
    private boolean streamingEnabled;
    public AgentServlet() {
        this(null);
    }
    public AgentServlet(Restrictor pRestrictor) {
        restrictor = pRestrictor;
    }
    protected LogHandler getLogHandler() {
        return logHandler;
    }
    @Override
    public void init(ServletConfig pServletConfig) throws ServletException {
        super.init(pServletConfig);
        Configuration config = initConfig(pServletConfig);
        String logHandlerClass =  config.get(ConfigKey.LOGHANDLER_CLASS);
        logHandler = logHandlerClass != null ?
                (LogHandler) ClassUtil.newInstance(logHandlerClass) :
                createLogHandler(pServletConfig,Boolean.valueOf(config.get(ConfigKey.DEBUG)));
        httpGetHandler = newGetHttpRequestHandler();
        httpPostHandler = newPostHttpRequestHandler();
        if (restrictor == null) {
            restrictor = createRestrictor(config);
        } else {
            logHandler.info("Using custom access restriction provided by " + restrictor);
        }
        configMimeType = config.get(ConfigKey.MIME_TYPE);
        backendManager = new BackendManager(config,logHandler, restrictor);
        requestHandler = new HttpRequestHandler(config,backendManager,logHandler);
        allowDnsReverseLookup = config.getAsBoolean(ConfigKey.ALLOW_DNS_REVERSE_LOOKUP);
        streamingEnabled = config.getAsBoolean(ConfigKey.STREAMING);
        initDiscoveryMulticast(config);
    }
    protected Restrictor createRestrictor(Configuration config) {
        return RestrictorFactory.createRestrictor(config, logHandler);
    }
    private void initDiscoveryMulticast(Configuration pConfig) {
        String url = findAgentUrl(pConfig);
        if (url != null || listenForDiscoveryMcRequests(pConfig)) {
            backendManager.getAgentDetails().setUrl(url);
            try {
                discoveryMulticastResponder = new DiscoveryMulticastResponder(backendManager,restrictor,logHandler);
                discoveryMulticastResponder.start();
            } catch (IOException e) {
                logHandler.error("Cannot start discovery multicast handler: " + e,e);
            }
        }
    }
    private String findAgentUrl(Configuration pConfig) {
        String url = System.getProperty("jolokia." + ConfigKey.DISCOVERY_AGENT_URL.getKeyValue());
        if (url == null) {
            url = System.getenv("JOLOKIA_DISCOVERY_AGENT_URL");
            if (url == null) {
                url = pConfig.get(ConfigKey.DISCOVERY_AGENT_URL);
            }
        }
        return NetworkUtil.replaceExpression(url);
    }
    private boolean listenForDiscoveryMcRequests(Configuration pConfig) {
        boolean sysProp = System.getProperty("jolokia." + ConfigKey.DISCOVERY_ENABLED.getKeyValue()) != null;
        boolean env     = System.getenv("JOLOKIA_DISCOVERY") != null;
        boolean config  = pConfig.getAsBoolean(ConfigKey.DISCOVERY_ENABLED);
        return sysProp || env || config;
    }
    protected LogHandler createLogHandler(ServletConfig pServletConfig, final boolean pDebug) {
        return new LogHandler() {
            public void debug(String message) {
                if (pDebug) {
                    log(message);
                }
            }
            public void info(String message) {
                log(message);
            }
            public void error(String message, Throwable t) {
                log(message,t);
            }
        };
    }
    @Override
    public void destroy() {
        backendManager.destroy();
        if (discoveryMulticastResponder != null) {
            discoveryMulticastResponder.stop();
            discoveryMulticastResponder = null;
        }
        super.destroy();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handle(httpGetHandler, req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handle(httpPostHandler, req, resp);
    }
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> responseHeaders =
                requestHandler.handleCorsPreflightRequest(
                        req.getHeader("Origin"),
                        req.getHeader("Access-Control-Request-Headers"));
        for (Map.Entry<String,String> entry : responseHeaders.entrySet()) {
            resp.setHeader(entry.getKey(),entry.getValue());
        }
    }
    @SuppressWarnings({ "PMD.AvoidCatchingThrowable", "PMD.AvoidInstanceofChecksInCatchClause" })
    private void handle(ServletRequestHandler pReqHandler,HttpServletRequest pReq, HttpServletResponse pResp) throws IOException {
        JSONAware json = null;
        try {
            requestHandler.checkAccess(allowDnsReverseLookup ? pReq.getRemoteHost() : null,
                                       pReq.getRemoteAddr(),
                                       getOriginOrReferer(pReq));
            validateCallbackIfGiven(pReq);
            updateAgentDetailsIfNeeded(pReq);
            json = handleSecurely(pReqHandler, pReq, pResp);
        } catch (Throwable exp) {
            try {
                json = requestHandler.handleThrowable(
                    exp instanceof RuntimeMBeanException ? ((RuntimeMBeanException) exp).getTargetException() : exp);
            } catch (Throwable exp2) {
                exp2.printStackTrace();
            }
        } finally {
            setCorsHeader(pReq, pResp);
            if (json == null) {
                json = requestHandler.handleThrowable(new Exception("Internal error while handling an exception"));
            }
            sendResponse(pResp, pReq, json);
        }
    }
    private JSONAware handleSecurely(final ServletRequestHandler pReqHandler, final HttpServletRequest pReq, final HttpServletResponse pResp) throws IOException, PrivilegedActionException {
        Subject subject = (Subject) pReq.getAttribute(ConfigKey.JAAS_SUBJECT_REQUEST_ATTRIBUTE);
        if (subject != null) {
            return Subject.doAs(subject, new PrivilegedExceptionAction<JSONAware>() {
                    public JSONAware run() throws IOException {
                        return pReqHandler.handleRequest(pReq, pResp);
                    }
            });
        } else {
            return pReqHandler.handleRequest(pReq, pResp);
        }
    }
    private String getOriginOrReferer(HttpServletRequest pReq) {
        String origin = pReq.getHeader("Origin");
        if (origin == null) {
            origin = pReq.getHeader("Referer");
        }
        return origin != null ? origin.replaceAll("[\\n\\r]*","") : null;
    }
    private void updateAgentDetailsIfNeeded(HttpServletRequest pReq) {
        AgentDetails details = backendManager.getAgentDetails();
        if (details.isInitRequired()) {
            synchronized (details) {
                if (details.isInitRequired()) {
                    if (details.isUrlMissing()) {
                        String url = getBaseUrl(NetworkUtil.sanitizeLocalUrl(pReq.getRequestURL().toString()),
                                extractServletPath(pReq));
                        details.setUrl(url);
                    }
                    if (details.isSecuredMissing()) {
                        details.setSecured(pReq.getAuthType() != null);
                    }
                    details.seal();
                }
            }
        }
    }
    private String extractServletPath(HttpServletRequest pReq) {
        return pReq.getRequestURI().substring(0,pReq.getContextPath().length());
    }
    private String getBaseUrl(String pUrl, String pServletPath) {
        String sUrl;
        try {
            URL url = new URL(pUrl);
            String host = getIpIfPossible(url.getHost());
            sUrl = new URL(url.getProtocol(),host,url.getPort(),pServletPath).toExternalForm();
        } catch (MalformedURLException exp) {
            sUrl = plainReplacement(pUrl, pServletPath);
        }
        return sUrl;
    }
    private String getIpIfPossible(String pHost) {
        try {
            InetAddress address = InetAddress.getByName(pHost);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return pHost;
        }
    }
    private String plainReplacement(String pUrl, String pServletPath) {
        int idx = pUrl.lastIndexOf(pServletPath);
        String url;
        if (idx != -1) {
            url = pUrl.substring(0,idx) + pServletPath;
        } else {
            url = pUrl;
        }
        return url;
    }
    private void setCorsHeader(HttpServletRequest pReq, HttpServletResponse pResp) {
        String origin = requestHandler.extractCorsOrigin(pReq.getHeader("Origin"));
        if (origin != null) {
            pResp.setHeader("Access-Control-Allow-Origin", origin);
            pResp.setHeader("Access-Control-Allow-Credentials","true");
        }
    }
    private boolean isStreamingEnabled(HttpServletRequest pReq) {
        String streamingFromReq = pReq.getParameter(ConfigKey.STREAMING.getKeyValue());
        if (streamingFromReq != null) {
            return Boolean.parseBoolean(streamingFromReq);
        }
        return streamingEnabled;
    }
    private interface ServletRequestHandler {
        JSONAware handleRequest(HttpServletRequest pReq, HttpServletResponse pResp)
                throws IOException;
    }
    private ServletRequestHandler newPostHttpRequestHandler() {
        return new ServletRequestHandler() {
             public JSONAware handleRequest(HttpServletRequest pReq, HttpServletResponse pResp)
                    throws IOException {
                 String encoding = pReq.getCharacterEncoding();
                 InputStream is = pReq.getInputStream();
                 return requestHandler.handlePostRequest(pReq.getRequestURI(),is, encoding, getParameterMap(pReq));
             }
        };
    }
    private ServletRequestHandler newGetHttpRequestHandler() {
        return new ServletRequestHandler() {
            public JSONAware handleRequest(HttpServletRequest pReq, HttpServletResponse pResp) {
                return requestHandler.handleGetRequest(pReq.getRequestURI(),pReq.getPathInfo(), getParameterMap(pReq));
            }
        };
    }
    private Map<String, String[]> getParameterMap(HttpServletRequest pReq){
        try {
            return pReq.getParameterMap();
        } catch (UnsupportedOperationException exp) {
            Map<String, String[]> ret = new HashMap<String, String[]>();
            Enumeration params = pReq.getParameterNames();
            while (params.hasMoreElements()) {
                String param = (String) params.nextElement();
                ret.put(param, pReq.getParameterValues(param));
            }
            return ret;
        }
    }
    Configuration initConfig(ServletConfig pConfig) {
        Configuration config = new Configuration(
                ConfigKey.AGENT_ID, NetworkUtil.getAgentId(hashCode(),"servlet"));
        config.updateGlobalConfiguration(new ServletConfigFacade(pConfig));
        config.updateGlobalConfiguration(new ServletContextFacade(getServletContext()));
        config.updateGlobalConfiguration(Collections.singletonMap(ConfigKey.AGENT_TYPE.getKeyValue(),"servlet"));
        return config;
    }
    private void sendResponse(HttpServletResponse pResp, HttpServletRequest pReq, JSONAware pJson) throws IOException {
        String callback = pReq.getParameter(ConfigKey.CALLBACK.getKeyValue());
        setContentType(pResp,
                       MimeTypeUtil.getResponseMimeType(
                           pReq.getParameter(ConfigKey.MIME_TYPE.getKeyValue()),
                           configMimeType, callback
                                                       ));
        pResp.setStatus(HttpServletResponse.SC_OK);
        setNoCacheHeaders(pResp);
        if (pJson == null) {
            pResp.setContentLength(-1);
        } else {
            if (isStreamingEnabled(pReq)) {
                sendStreamingResponse(pResp, callback, (JSONStreamAware) pJson);
            } else {
                sendAllJSON(pResp, callback, pJson);
            }
        }
    }
    private void validateCallbackIfGiven(HttpServletRequest pReq) {
        String callback = pReq.getParameter(ConfigKey.CALLBACK.getKeyValue());
        if (callback != null && !MimeTypeUtil.isValidCallback(callback)) {
            throw new IllegalArgumentException("Invalid callback name given, which must be a valid javascript function name");
        }
    }
    private void sendStreamingResponse(HttpServletResponse pResp, String pCallback, JSONStreamAware pJson) throws IOException {
        Writer writer = new OutputStreamWriter(pResp.getOutputStream(), "UTF-8");
        IoUtil.streamResponseAndClose(writer, pJson, pCallback);
    }
    private void sendAllJSON(HttpServletResponse pResp, String callback, JSONAware pJson) throws IOException {
        OutputStream out = null;
        try {
            String json = pJson.toJSONString();
            String content = callback == null ? json : callback + "(" + json + ");";
            byte[] response = content.getBytes("UTF8");
            pResp.setContentLength(response.length);
            out = pResp.getOutputStream();
            out.write(response);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    private void setNoCacheHeaders(HttpServletResponse pResp) {
        pResp.setHeader("Cache-Control", "no-cache");
        pResp.setHeader("Pragma","no-cache");
        long now = System.currentTimeMillis();
        pResp.setDateHeader("Date",now);
        pResp.setDateHeader("Expires",now - 3600000);
    }
    private void setContentType(HttpServletResponse pResp, String pContentType) {
        boolean encodingDone = false;
        try {
            pResp.setCharacterEncoding("utf-8");
            pResp.setContentType(pContentType);
            encodingDone = true;
        }
        catch (NoSuchMethodError error) {  }
        catch (UnsupportedOperationException error) {  }
        if (!encodingDone) {
            pResp.setContentType(pContentType + "; charset=utf-8");
        }
    }
    private static final class ServletConfigFacade implements ConfigExtractor {
        private final ServletConfig config;
        private ServletConfigFacade(ServletConfig pConfig) {
            config = pConfig;
        }
        public Enumeration getNames() {
            return config.getInitParameterNames();
        }
        public String getParameter(String pName) {
            return config.getInitParameter(pName);
        }
    }
    private static final class ServletContextFacade implements ConfigExtractor {
        private final ServletContext servletContext;
        private ServletContextFacade(ServletContext pServletContext) {
            servletContext = pServletContext;
        }
        public Enumeration getNames() {
            return servletContext.getInitParameterNames();
        }
        public String getParameter(String pName) {
            return servletContext.getInitParameter(pName);
        }
    }
}
