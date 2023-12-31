
package org.jasig.cas.client.validation;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.jasig.cas.client.ssl.HttpURLConnectionFactory;
import org.jasig.cas.client.ssl.HttpsURLConnectionFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class AbstractUrlBasedTicketValidator implements TicketValidator {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private HttpURLConnectionFactory urlConnectionFactory = new HttpsURLConnectionFactory();
    private final String casServerUrlPrefix;
    private boolean renew;
    private Map<String, String> customParameters;
    private String encoding;
    protected AbstractUrlBasedTicketValidator(final String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
        CommonUtils.assertNotNull(this.casServerUrlPrefix, "casServerUrlPrefix cannot be null.");
    }
    protected void populateUrlAttributeMap(final Map<String, String> urlParameters) {
    }
    protected abstract String getUrlSuffix();
    protected abstract void setDisableXmlSchemaValidation(boolean disabled);
    protected final String constructValidationUrl(final String ticket, final String serviceUrl) {
        final Map<String, String> urlParameters = new HashMap<String, String>();
        logger.debug("Placing URL parameters in map.");
        urlParameters.put("ticket", ticket);
        urlParameters.put("service", serviceUrl);
        if (this.renew) {
            urlParameters.put("renew", "true");
        }
        logger.debug("Calling template URL attribute map.");
        populateUrlAttributeMap(urlParameters);
        logger.debug("Loading custom parameters from configuration.");
        if (this.customParameters != null) {
            urlParameters.putAll(this.customParameters);
        }
        final String suffix = getUrlSuffix();
        final StringBuilder buffer = new StringBuilder(urlParameters.size() * 10 + this.casServerUrlPrefix.length()
                + suffix.length() + 1);
        int i = 0;
        buffer.append(this.casServerUrlPrefix);
        if (!this.casServerUrlPrefix.endsWith("/")) {
            buffer.append("/");
        }
        buffer.append(suffix);
        for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            if (value != null) {
                buffer.append(i++ == 0 ? "?" : "&");
                buffer.append(key);
                buffer.append("=");
                final String encodedValue = encodeUrl(value);
                buffer.append(encodedValue);
            }
        }
        return buffer.toString();
    }
    protected final String encodeUrl(final String url) {
        if (url == null) {
            return null;
        }
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return url;
        }
    }
    protected abstract Assertion parseResponseFromServer(final String response) throws TicketValidationException;
    protected abstract String retrieveResponseFromServer(URL validationUrl, String ticket);
    public final Assertion validate(final String ticket, final String service) throws TicketValidationException {
        final String validationUrl = constructValidationUrl(ticket, service);
        logger.debug("Constructing validation url: {}", validationUrl);
        try {
            logger.debug("Retrieving response from server.");
            final String serverResponse = retrieveResponseFromServer(new URL(validationUrl), ticket);
            if (serverResponse == null) {
                throw new TicketValidationException("The CAS server returned no response.");
            }
            logger.debug("Server response: {}", serverResponse);
            return parseResponseFromServer(serverResponse);
        } catch (final MalformedURLException e) {
            throw new TicketValidationException(e);
        }
    }
    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }
    public final void setCustomParameters(final Map<String, String> customParameters) {
        this.customParameters = customParameters;
    }
    public final void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
    protected final String getEncoding() {
        return this.encoding;
    }
    protected final boolean isRenew() {
        return this.renew;
    }
    protected final String getCasServerUrlPrefix() {
        return this.casServerUrlPrefix;
    }
    protected final Map<String, String> getCustomParameters() {
        return this.customParameters;
    }
    protected HttpURLConnectionFactory getURLConnectionFactory() {
        return this.urlConnectionFactory;
    }
    public void setURLConnectionFactory(final HttpURLConnectionFactory urlConnectionFactory) {
        this.urlConnectionFactory = urlConnectionFactory;
    }
}
