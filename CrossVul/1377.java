
package com.sun.faces.lifecycle;
import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.render.ResponseStateManager;
import javax.faces.FacesException;
public class ClientWindowImpl extends ClientWindow {
    String id;
    public ClientWindowImpl() {
    }
    @Override
    public Map<String, String> getQueryURLParameters(FacesContext context) {
        return null;
    }
    @Override
    public void decode(FacesContext context) {
        Map<String, String> requestParamMap = context.getExternalContext().getRequestParameterMap();
        if (isClientWindowRenderModeEnabled(context)) {
            id = requestParamMap.get(ResponseStateManager.CLIENT_WINDOW_URL_PARAM);
        }
        if (requestParamMap.containsKey(ResponseStateManager.CLIENT_WINDOW_PARAM)) {
            id = requestParamMap.get(ResponseStateManager.CLIENT_WINDOW_PARAM);
            Pattern safePattern = Pattern.compile(".*<(.*:script|script).*>[^&]*</\\s*\\1\\s*>.*");
            if (safePattern.matcher(id).matches()) {
                throw new FacesException("ClientWindow is illegal: " + id);
            }
        }
        if (null == id) {
            id = calculateClientWindow(context);
        }
    }
    private String calculateClientWindow(FacesContext context) {
        synchronized(context.getExternalContext().getSession(true)) {
            final String clientWindowCounterKey = "com.sun.faces.lifecycle.ClientWindowCounterKey";
            ExternalContext extContext = context.getExternalContext();
            Map<String, Object> sessionAttrs = extContext.getSessionMap();
            Integer counter = (Integer) sessionAttrs.get(clientWindowCounterKey);
            if (null == counter) {
                counter = Integer.valueOf(0);
            }
            char sep = UINamingContainer.getSeparatorChar(context);
            id = extContext.getSessionId(true) + sep +
                    + counter;
            sessionAttrs.put(clientWindowCounterKey, ++counter);
        }
        return id;
    }
    @Override
    public String getId() {
        return id;
    }
}
