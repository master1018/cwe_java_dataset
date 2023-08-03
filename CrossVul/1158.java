package com.zrlog.web.handler;
import com.hibegin.common.util.IOUtil;
import com.hibegin.common.util.http.HttpUtil;
import com.hibegin.common.util.http.handle.CloseResponseHandle;
import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.zrlog.common.Constants;
import com.zrlog.common.vo.AdminTokenVO;
import com.zrlog.model.User;
import com.zrlog.service.AdminTokenService;
import com.zrlog.service.AdminTokenThreadLocal;
import com.zrlog.service.PluginHelper;
import com.zrlog.util.BlogBuildInfoUtil;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PluginHandler extends Handler {
    private static final Logger LOGGER = Logger.getLogger(PluginHandler.class);
    private AdminTokenService adminTokenService = new AdminTokenService();
    private List<String> pluginHandlerPaths = Arrays.asList("/admin/plugins/", "/plugin/", "/p/");
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        response.addHeader("X-ZrLog", BlogBuildInfoUtil.getVersion());
        boolean isPluginPath = false;
        for (String path : pluginHandlerPaths) {
            if (target.startsWith(path)) {
                isPluginPath = true;
            }
        }
        if (isPluginPath) {
            try {
                Map.Entry<AdminTokenVO, User> entry = adminTokenService.getAdminTokenVOUserEntry(request);
                if (entry != null) {
                    adminTokenService.setAdminToken(entry.getValue(), entry.getKey().getSessionId(), entry.getKey().getProtocol(), request, response);
                }
                if (target.startsWith("/admin/plugins/")) {
                    try {
                        adminPermission(target, request, response);
                    } catch (IOException | InstantiationException e) {
                        LOGGER.error(e);
                    }
                } else if (target.startsWith("/plugin/") || target.startsWith("/p/")) {
                    try {
                        visitorPermission(target, request, response);
                    } catch (IOException | InstantiationException e) {
                        LOGGER.error(e);
                    }
                }
            } finally {
                isHandled[0] = true;
            }
        } else {
            this.next.handle(target, request, response, isHandled);
        }
    }
    private void adminPermission(String target, HttpServletRequest request, HttpServletResponse response) throws IOException, InstantiationException {
        if (AdminTokenThreadLocal.getUser() != null) {
            accessPlugin(target.replace("/admin/plugins", ""), request, response);
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/admin/login?redirectFrom="
                    + request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        }
    }
    private void visitorPermission(String target, HttpServletRequest request, HttpServletResponse response) throws IOException, InstantiationException {
        if (!accessPlugin(target.replace("/plugin", "").replace("/p", ""), request, response)) {
            response.sendError(403);
        }
    }
    private boolean accessPlugin(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, InstantiationException {
        CloseResponseHandle handle = getContext(uri, request.getMethod(), request, true);
        try {
            if (handle.getT() != null && handle.getT().getEntity() != null) {
                response.setStatus(handle.getT().getStatusLine().getStatusCode());
                handle.getT().removeHeaders("Transfer-Encoding");
                for (Header header : handle.getT().getAllHeaders()) {
                    response.addHeader(header.getName(), header.getValue());
                }
                byte[] bytes = IOUtil.getByteByInputStream(handle.getT().getEntity().getContent());
                response.addHeader("Content-Length", Integer.valueOf(bytes.length).toString());
                response.getOutputStream().write(bytes);
                response.getOutputStream().close();
                return true;
            } else {
                return false;
            }
        } finally {
            handle.close();
        }
    }
    public static CloseResponseHandle getContext(String uri, String method, HttpServletRequest request, boolean disableRedirect) throws IOException, InstantiationException {
        String pluginServerHttp = Constants.pluginServer;
        CloseableHttpResponse httpResponse;
        CloseResponseHandle handle = new CloseResponseHandle();
        HttpUtil httpUtil = disableRedirect ? HttpUtil.getDisableRedirectInstance() : HttpUtil.getInstance();
        if (method.equals(request.getMethod()) && "GET".equalsIgnoreCase(method)) {
            httpResponse = httpUtil.sendGetRequest(pluginServerHttp + uri, request.getParameterMap(), handle, PluginHelper.genHeaderMapByRequest(request)).getT();
        } else {
            if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
                httpResponse = httpUtil.sendPostRequest(pluginServerHttp + uri, request.getParameterMap(), handle, PluginHelper.genHeaderMapByRequest(request)).getT();
            } else {
                httpResponse = httpUtil.sendPostRequest(pluginServerHttp + uri + "?" + request.getQueryString(), IOUtil.getByteByInputStream(request.getInputStream()), handle, PluginHelper.genHeaderMapByRequest(request)).getT();
            }
        }
        if (httpResponse != null) {
            Map<String, String> headerMap = new HashMap<>();
            Header[] headers = httpResponse.getAllHeaders();
            for (Header header : headers) {
                headerMap.put(header.getName(), header.getValue());
            }
            if (JFinal.me().getConstants().getDevMode()) {
                LOGGER.info(uri + " --------------------------------- response");
            }
            for (Map.Entry<String, String> t : headerMap.entrySet()) {
                if (JFinal.me().getConstants().getDevMode()) {
                    LOGGER.info("key " + t.getKey() + " value-> " + t.getValue());
                }
            }
        }
        return handle;
    }
}
