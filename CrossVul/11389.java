package org.jolokia.jvmagent.handler;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.MalformedObjectNameException;
import javax.management.RuntimeMBeanException;
import javax.security.auth.Subject;
import com.sun.net.httpserver.*;
import org.jolokia.backend.BackendManager;
import org.jolokia.config.ConfigKey;
import org.jolokia.config.Configuration;
import org.jolokia.discovery.AgentDetails;
import org.jolokia.discovery.DiscoveryMulticastResponder;
import org.jolokia.http.HttpRequestHandler;
import org.jolokia.jvmagent.ParsedUri;
import org.jolokia.restrictor.*;
import org.jolokia.util.*;
import org.json.simple.JSONAware;
import org.json.simple.JSONStreamAware;
public class JolokiaHttpHandler implements HttpHandler {
    private BackendManager backendManager;
    private HttpRequestHandler requestHandler;
    private String context;
    private Pattern contentTypePattern = Pattern.compile(".*;\\s*charset=([^;,]+)\\s*.*");
    private Configuration configuration;
    private final LogHandler logHandler;
    private DiscoveryMulticastResponder discoveryMulticastResponder;
    public JolokiaHttpHandler(Configuration pConfig) {
        this(pConfig, null);
    }
    public JolokiaHttpHandler(Configuration pConfig, LogHandler pLogHandler) {
        configuration = pConfig;
        context = pConfig.get(ConfigKey.AGENT_CONTEXT);
        if (!context.endsWith("/")) {
            context += "/";
        }
        logHandler = pLogHandler != null ? pLogHandler : createLogHandler(pConfig.get(ConfigKey.LOGHANDLER_CLASS), pConfig.get(ConfigKey.DEBUG));
    }
    public void start(boolean pLazy) {
        Restrictor restrictor = createRestrictor();
        backendManager = new BackendManager(configuration, logHandler, restrictor, pLazy);
        requestHandler = new HttpRequestHandler(configuration, backendManager, logHandler);
        if (listenForDiscoveryMcRequests(configuration)) {
            try {
                discoveryMulticastResponder = new DiscoveryMulticastResponder(backendManager, restrictor, logHandler);
                discoveryMulticastResponder.start();
            } catch (IOException e) {
                logHandler.error("Cannot start discovery multicast handler: " + e, e);
            }
        }
    }
    protected Restrictor createRestrictor() {
        return RestrictorFactory.createRestrictor(configuration, logHandler);
    }
    private boolean listenForDiscoveryMcRequests(Configuration pConfig) {
        String enable = pConfig.get(ConfigKey.DISCOVERY_ENABLED);
        String url = pConfig.get(ConfigKey.DISCOVERY_AGENT_URL);
        return url != null || enable == null || Boolean.valueOf(enable);
    }
    public void start(boolean pLazy, String pUrl, boolean pSecured) {
        start(pLazy);
        AgentDetails details = backendManager.getAgentDetails();
        details.setUrl(pUrl);
        details.setSecured(pSecured);
    }
    public void stop() {
        if (discoveryMulticastResponder != null) {
            discoveryMulticastResponder.stop();
            discoveryMulticastResponder = null;
        }
        backendManager.destroy();
        backendManager = null;
        requestHandler = null;
    }
    public void handle(final HttpExchange pHttpExchange) throws IOException {
        try {
            checkAuthentication(pHttpExchange);
            Subject subject = (Subject) pHttpExchange.getAttribute(ConfigKey.JAAS_SUBJECT_REQUEST_ATTRIBUTE);
            if (subject != null)  {
                doHandleAs(subject, pHttpExchange);
            }  else {
                doHandle(pHttpExchange);
            }
        } catch (SecurityException exp) {
            sendForbidden(pHttpExchange,exp);
        }
    }
    private void doHandleAs(Subject subject, final HttpExchange pHttpExchange) {
        try {
            Subject.doAs(subject, new PrivilegedExceptionAction<Void>() {
            public Void run() throws IOException {
                doHandle(pHttpExchange);
                return null;
            }
            });
        } catch (PrivilegedActionException e) {
            throw new SecurityException("Security exception: " + e.getCause(),e.getCause());
        }
    }
    protected void checkAuthentication(HttpExchange pHttpExchange) throws SecurityException { }
    @SuppressWarnings({"PMD.AvoidCatchingThrowable", "PMD.AvoidInstanceofChecksInCatchClause"})
    public void doHandle(HttpExchange pExchange) throws IOException {
        if (requestHandler == null) {
            throw new IllegalStateException("Handler not yet started");
        }
        JSONAware json = null;
        URI uri = pExchange.getRequestURI();
        ParsedUri parsedUri = new ParsedUri(uri, context);
        try {
            InetSocketAddress address = pExchange.getRemoteAddress();
            requestHandler.checkAccess(getHostName(address),
                                       address.getAddress().getHostAddress(),
                                       extractOriginOrReferer(pExchange));
            String method = pExchange.getRequestMethod();
            validateCallbackIfGiven(parsedUri);
            if ("GET".equalsIgnoreCase(method)) {
                setHeaders(pExchange);
                json = executeGetRequest(parsedUri);
            } else if ("POST".equalsIgnoreCase(method)) {
                setHeaders(pExchange);
                json = executePostRequest(pExchange, parsedUri);
            } else if ("OPTIONS".equalsIgnoreCase(method)) {
                performCorsPreflightCheck(pExchange);
            } else {
                throw new IllegalArgumentException("HTTP Method " + method + " is not supported.");
            }
        } catch (Throwable exp) {
            json = requestHandler.handleThrowable(
                    exp instanceof RuntimeMBeanException ? ((RuntimeMBeanException) exp).getTargetException() : exp);
        } finally {
            sendResponse(pExchange, parsedUri, json);
        }
    }
    private void validateCallbackIfGiven(ParsedUri pUri) {
        String callback = pUri.getParameter(ConfigKey.CALLBACK.getKeyValue());
        if (callback != null && !MimeTypeUtil.isValidCallback(callback)) {
            throw new IllegalArgumentException("Invalid callback name given, which must be a valid javascript function name");
        }
    }
    private String extractOriginOrReferer(HttpExchange pExchange) {
        Headers headers = pExchange.getRequestHeaders();
        String origin = headers.getFirst("Origin");
        if (origin == null) {
            origin = headers.getFirst("Referer");
        }
        return origin != null ? origin.replaceAll("[\\n\\r]*","") : null;
    }
    private String getHostName(InetSocketAddress address) {
        return configuration.getAsBoolean(ConfigKey.ALLOW_DNS_REVERSE_LOOKUP) ? address.getHostName() : null;
    }
    private JSONAware executeGetRequest(ParsedUri parsedUri) {
        return requestHandler.handleGetRequest(parsedUri.getUri().toString(),parsedUri.getPathInfo(), parsedUri.getParameterMap());
    }
    private JSONAware executePostRequest(HttpExchange pExchange, ParsedUri pUri) throws MalformedObjectNameException, IOException {
        String encoding = null;
        Headers headers = pExchange.getRequestHeaders();
        String cType =  headers.getFirst("Content-Type");
        if (cType != null) {
            Matcher matcher = contentTypePattern.matcher(cType);
            if (matcher.matches()) {
                encoding = matcher.group(1);
            }
        }
        InputStream is = pExchange.getRequestBody();
        return requestHandler.handlePostRequest(pUri.toString(),is, encoding, pUri.getParameterMap());
    }
    private void performCorsPreflightCheck(HttpExchange pExchange) {
        Headers requestHeaders = pExchange.getRequestHeaders();
        Map<String,String> respHeaders =
                requestHandler.handleCorsPreflightRequest(requestHeaders.getFirst("Origin"),
                                                          requestHeaders.getFirst("Access-Control-Request-Headers"));
        Headers responseHeaders = pExchange.getResponseHeaders();
        for (Map.Entry<String,String> entry : respHeaders.entrySet()) {
            responseHeaders.set(entry.getKey(), entry.getValue());
        }
    }
    private void setHeaders(HttpExchange pExchange) {
        String origin = requestHandler.extractCorsOrigin(pExchange.getRequestHeaders().getFirst("Origin"));
        Headers headers = pExchange.getResponseHeaders();
        if (origin != null) {
            headers.set("Access-Control-Allow-Origin",origin);
            headers.set("Access-Control-Allow-Credentials","true");
        }
        headers.set("Cache-Control", "no-cache");
        headers.set("Pragma","no-cache");
        Calendar cal = Calendar.getInstance();
        headers.set("Date",formatHeaderDate(cal.getTime()));
        cal.add(Calendar.HOUR, -1);
        headers.set("Expires",formatHeaderDate(cal.getTime()));
    }
    private void sendForbidden(HttpExchange pExchange, SecurityException securityException) throws IOException {
        String response = "403 (Forbidden)\n";
        if (securityException != null && securityException.getMessage() != null) {
            response += "\n" + securityException.getMessage() + "\n";
        }
        pExchange.sendResponseHeaders(403, response.length());
        OutputStream os = pExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private void sendResponse(HttpExchange pExchange, ParsedUri pParsedUri, JSONAware pJson) throws IOException {
        boolean streaming = configuration.getAsBoolean(ConfigKey.STREAMING);
        if (streaming) {
            JSONStreamAware jsonStream = (JSONStreamAware)pJson;
            sendStreamingResponse(pExchange, pParsedUri, jsonStream);
        } else {
            sendAllJSON(pExchange, pParsedUri, pJson);
        }
    }
    private void sendStreamingResponse(HttpExchange pExchange, ParsedUri pParsedUri, JSONStreamAware pJson) throws IOException {
        Headers headers = pExchange.getResponseHeaders();
        if (pJson != null) {
            headers.set("Content-Type", getMimeType(pParsedUri) + "; charset=utf-8");
            pExchange.sendResponseHeaders(200, 0);
            Writer writer = new OutputStreamWriter(pExchange.getResponseBody(), "UTF-8");
            String callback = pParsedUri.getParameter(ConfigKey.CALLBACK.getKeyValue());
            IoUtil.streamResponseAndClose(writer, pJson, callback != null && MimeTypeUtil.isValidCallback(callback) ? callback : null);
        } else {
            headers.set("Content-Type", "text/plain");
            pExchange.sendResponseHeaders(200,-1);
        }
    }
    private void sendAllJSON(HttpExchange pExchange, ParsedUri pParsedUri, JSONAware pJson) throws IOException {
        OutputStream out = null;
        try {
            Headers headers = pExchange.getResponseHeaders();
            if (pJson != null) {
                headers.set("Content-Type", getMimeType(pParsedUri) + "; charset=utf-8");
                String json = pJson.toJSONString();
                String callback = pParsedUri.getParameter(ConfigKey.CALLBACK.getKeyValue());
                String content = callback != null && MimeTypeUtil.isValidCallback(callback) ? callback + "(" + json + ");" : json;
                byte[] response = content.getBytes("UTF8");
                pExchange.sendResponseHeaders(200,response.length);
                out = pExchange.getResponseBody();
                out.write(response);
            } else {
                headers.set("Content-Type", "text/plain");
                pExchange.sendResponseHeaders(200,-1);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    private String getMimeType(ParsedUri pParsedUri) {
        return MimeTypeUtil.getResponseMimeType(
            pParsedUri.getParameter(ConfigKey.MIME_TYPE.getKeyValue()),
            configuration.get(ConfigKey.MIME_TYPE),
            pParsedUri.getParameter(ConfigKey.CALLBACK.getKeyValue()));
    }
    private LogHandler createLogHandler(String pLogHandlerClass, String pDebug) {
        if (pLogHandlerClass != null) {
            return ClassUtil.newInstance(pLogHandlerClass);
        } else {
            final boolean debug = Boolean.valueOf(pDebug);
            return new LogHandler.StdoutLogHandler(debug);
        }
    }
    private String formatHeaderDate(Date date) {
        DateFormat rfc1123Format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        rfc1123Format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return rfc1123Format.format(date);
    }
}
