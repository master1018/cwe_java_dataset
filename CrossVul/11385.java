package org.jolokia.util;
import java.util.regex.Pattern;
public class MimeTypeUtil {
    public static String getResponseMimeType(String pRequestMimeType, String defaultMimeType, String pCallback) {
        if (pCallback != null && isValidCallback(pCallback)) {
            return "text/javascript";
        }
        if (pRequestMimeType != null) {
            return sanitize(pRequestMimeType);
        }
        return sanitize(defaultMimeType);
    }
    private static String sanitize(String mimeType) {
        for (String accepted : new String[]{
            "application/json",
            "text/plain"
        }) {
            if (accepted.equalsIgnoreCase(mimeType)) {
                return accepted;
            }
        }
        return "text/plain";
    }
    public static boolean isValidCallback(String pCallback) {
        Pattern validJavaScriptFunctionNamePattern =
            Pattern.compile("^[$A-Z_][0-9A-Z_$]*$", Pattern.CASE_INSENSITIVE);
        return validJavaScriptFunctionNamePattern.matcher(pCallback).matches();
    }
}