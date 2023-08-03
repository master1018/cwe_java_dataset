package org.jolokia.http;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.*;
import org.jolokia.backend.BackendManager;
import org.jolokia.config.*;
import org.jolokia.request.JmxRequest;
import org.jolokia.request.JmxRequestFactory;
import org.jolokia.util.LogHandler;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class HttpRequestHandler {
    private BackendManager backendManager;
    private LogHandler logHandler;
    private Configuration config;
    public HttpRequestHandler(Configuration pConfig, BackendManager pBackendManager, LogHandler pLogHandler) {
        backendManager = pBackendManager;
        logHandler = pLogHandler;
        config = pConfig;
    }
    public JSONAware handleGetRequest(String pUri, String pPathInfo, Map<String, String[]> pParameterMap) {
        String pathInfo = extractPathInfo(pUri, pPathInfo);
        JmxRequest jmxReq =
                JmxRequestFactory.createGetRequest(pathInfo,getProcessingParameter(pParameterMap));
        if (backendManager.isDebug()) {
            logHandler.debug("URI: " + pUri);
            logHandler.debug("Path-Info: " + pathInfo);
            logHandler.debug("Request: " + jmxReq.toString());
        }
        return executeRequest(jmxReq);
    }
    private ProcessingParameters getProcessingParameter(Map<String, String[]> pParameterMap) {
        Map<String,String> ret = new HashMap<String, String>();
        if (pParameterMap != null) {
            for (Map.Entry<String,String[]> entry : pParameterMap.entrySet()) {
                String values[] = entry.getValue();
                if (values != null && values.length > 0) {
                        ret.put(entry.getKey(), values[0]);
                }
            }
        }
        return config.getProcessingParameters(ret);
    }
    public JSONAware handlePostRequest(String pUri, InputStream pInputStream, String pEncoding, Map<String, String[]>  pParameterMap)
            throws IOException {
        if (backendManager.isDebug()) {
            logHandler.debug("URI: " + pUri);
        }
        Object jsonRequest = extractJsonRequest(pInputStream,pEncoding);
        if (jsonRequest instanceof JSONArray) {
            List<JmxRequest> jmxRequests = JmxRequestFactory.createPostRequests((List) jsonRequest,getProcessingParameter(pParameterMap));
            JSONArray responseList = new JSONArray();
            for (JmxRequest jmxReq : jmxRequests) {
                if (backendManager.isDebug()) {
                    logHandler.debug("Request: " + jmxReq.toString());
                }
                JSONObject resp = executeRequest(jmxReq);
                responseList.add(resp);
            }
            return responseList;
        } else if (jsonRequest instanceof JSONObject) {
            JmxRequest jmxReq = JmxRequestFactory.createPostRequest((Map<String, ?>) jsonRequest,getProcessingParameter(pParameterMap));
            return executeRequest(jmxReq);
        } else {
            throw new IllegalArgumentException("Invalid JSON Request " + jsonRequest);
        }
    }
    public Map<String, String> handleCorsPreflightRequest(String pOrigin, String pRequestHeaders) {
        Map<String,String> ret = new HashMap<String, String>();
        if (pOrigin != null && backendManager.isCorsAccessAllowed(pOrigin)) {
            ret.put("Access-Control-Allow-Origin","null".equals(pOrigin) ? "*" : pOrigin);
            if (pRequestHeaders != null) {
                ret.put("Access-Control-Allow-Headers",pRequestHeaders);
            }
            ret.put("Access-Control-Allow-Credentials","true");
            ret.put("Access-Control-Allow-Max-Age","" + 3600 * 24 * 365);
        }
        return ret;
    }
    private Object extractJsonRequest(InputStream pInputStream, String pEncoding) throws IOException {
        InputStreamReader reader = null;
        try {
            reader =
                    pEncoding != null ?
                            new InputStreamReader(pInputStream, pEncoding) :
                            new InputStreamReader(pInputStream);
            JSONParser parser = new JSONParser();
            return parser.parse(reader);
        } catch (ParseException exp) {
            throw new IllegalArgumentException("Invalid JSON request " + reader,exp);
        }
    }
    private JSONObject executeRequest(JmxRequest pJmxReq) {
        try {
            return backendManager.handleRequest(pJmxReq);
        } catch (ReflectionException e) {
            return getErrorJSON(404,e, pJmxReq);
        } catch (InstanceNotFoundException e) {
            return getErrorJSON(404,e, pJmxReq);
        } catch (MBeanException e) {
            return getErrorJSON(500,e.getTargetException(), pJmxReq);
        } catch (AttributeNotFoundException e) {
            return getErrorJSON(404,e, pJmxReq);
        } catch (UnsupportedOperationException e) {
            return getErrorJSON(500,e, pJmxReq);
        } catch (IOException e) {
            return getErrorJSON(500,e, pJmxReq);
        } catch (IllegalArgumentException e) {
            return getErrorJSON(400,e, pJmxReq);
        } catch (SecurityException e) {
            return getErrorJSON(403,new Exception(e.getMessage()), pJmxReq);
        } catch (RuntimeMBeanException e) {
            return errorForUnwrappedException(e,pJmxReq);
        }
    }
    public JSONObject handleThrowable(Throwable pThrowable) {
        if (pThrowable instanceof IllegalArgumentException) {
            return getErrorJSON(400,pThrowable, null);
        } else if (pThrowable instanceof SecurityException) {
            return getErrorJSON(403,new Exception(pThrowable.getMessage()), null);
        } else {
            return getErrorJSON(500,pThrowable, null);
        }
    }
    public JSONObject getErrorJSON(int pErrorCode, Throwable pExp, JmxRequest pJmxReq) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",pErrorCode);
        jsonObject.put("error",getExceptionMessage(pExp));
        jsonObject.put("error_type", pExp.getClass().getName());
        addErrorInfo(jsonObject, pExp, pJmxReq);
        if (backendManager.isDebug()) {
            backendManager.error("Error " + pErrorCode,pExp);
        }
        if (pJmxReq != null) {
            jsonObject.put("request",pJmxReq.toJSON());
        }
        return jsonObject;
    }
    public void checkClientIPAccess(String pHost, String pAddress) {
        if (!backendManager.isRemoteAccessAllowed(pHost,pAddress)) {
            throw new SecurityException("No access from client " + pAddress + " allowed");
        }
    }
    public String extractCorsOrigin(String pOrigin) {
        if (pOrigin != null) {
            String origin  = pOrigin.replaceAll("[\\n\\r]*","");
            if (backendManager.isCorsAccessAllowed(origin)) {
                return "null".equals(origin) ? "*" : origin;
            } else {
                return null;
            }
        }
        return null;
    }
    private void addErrorInfo(JSONObject pErrorResp, Throwable pExp, JmxRequest pJmxReq) {
        String includeStackTrace = pJmxReq != null ?
                pJmxReq.getParameter(ConfigKey.INCLUDE_STACKTRACE) : "true";
        if (includeStackTrace.equalsIgnoreCase("true") ||
            (includeStackTrace.equalsIgnoreCase("runtime") && pExp instanceof RuntimeException)) {
            StringWriter writer = new StringWriter();
            pExp.printStackTrace(new PrintWriter(writer));
            pErrorResp.put("stacktrace",writer.toString());
        }
        if (pJmxReq != null && pJmxReq.getParameterAsBool(ConfigKey.SERIALIZE_EXCEPTION)) {
            pErrorResp.put("error_value",backendManager.convertExceptionToJson(pExp,pJmxReq));
        }
    }
    private String getExceptionMessage(Throwable pException) {
        String message = pException.getLocalizedMessage();
        return pException.getClass().getName() + (message != null ? " : " + message : "");
    }
    private JSONObject errorForUnwrappedException(Exception e, JmxRequest pJmxReq) {
        Throwable cause = e.getCause();
        int code = cause instanceof IllegalArgumentException ? 400 : cause instanceof SecurityException ? 403 : 500;
        return getErrorJSON(code,cause, pJmxReq);
    }
    private static final Pattern PATH_PREFIX_PATTERN = Pattern.compile("^/?[^/]+/");
    private String extractPathInfo(String pUri, String pPathInfo) {
        if (pUri.contains("!
            Matcher matcher = PATH_PREFIX_PATTERN.matcher(pPathInfo);
            if (matcher.find()) {
                String prefix = matcher.group();
                String pathInfoEncoded = pUri.replaceFirst("^.*?" + prefix, prefix);
                try {
                    return URLDecoder.decode(pathInfoEncoded, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return pPathInfo;
    }
}
