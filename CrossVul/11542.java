
package org.jasig.cas.client.validation;
import java.io.StringReader;
import java.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.proxy.Cas20ProxyRetriever;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyRetriever;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
public class Cas20ServiceTicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {
    private String proxyCallbackUrl;
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;
    private ProxyRetriever proxyRetriever;
    public Cas20ServiceTicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
        this.proxyRetriever = new Cas20ProxyRetriever(casServerUrlPrefix, getEncoding(), getURLConnectionFactory());
    }
    protected final void populateUrlAttributeMap(final Map<String, String> urlParameters) {
        urlParameters.put("pgtUrl", this.proxyCallbackUrl);
    }
    protected String getUrlSuffix() {
        return "serviceValidate";
    }
    protected final Assertion parseResponseFromServer(final String response) throws TicketValidationException {
        final String error = XmlUtils.getTextForElement(response, "authenticationFailure");
        if (CommonUtils.isNotBlank(error)) {
            throw new TicketValidationException(error);
        }
        final String principal = XmlUtils.getTextForElement(response, "user");
        final String proxyGrantingTicketIou = XmlUtils.getTextForElement(response, "proxyGrantingTicket");
        final String proxyGrantingTicket;
        if (CommonUtils.isBlank(proxyGrantingTicketIou) || this.proxyGrantingTicketStorage == null) {
            proxyGrantingTicket = null;
        } else {
            proxyGrantingTicket = this.proxyGrantingTicketStorage.retrieve(proxyGrantingTicketIou);
        }
        if (CommonUtils.isEmpty(principal)) {
            throw new TicketValidationException("No principal was found in the response from the CAS server.");
        }
        final Assertion assertion;
        final Map<String, Object> attributes = extractCustomAttributes(response);
        if (CommonUtils.isNotBlank(proxyGrantingTicket)) {
            final AttributePrincipal attributePrincipal = new AttributePrincipalImpl(principal, attributes,
                    proxyGrantingTicket, this.proxyRetriever);
            assertion = new AssertionImpl(attributePrincipal);
        } else {
            assertion = new AssertionImpl(new AttributePrincipalImpl(principal, attributes));
        }
        customParseResponse(response, assertion);
        return assertion;
    }
    protected Map<String, Object> extractCustomAttributes(final String xml) {
        final SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        try {
            final SAXParser saxParser = spf.newSAXParser();
            final XMLReader xmlReader = saxParser.getXMLReader();
            final CustomAttributeHandler handler = new CustomAttributeHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xml)));
            return handler.getAttributes();
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyMap();
        }
    }
    protected void customParseResponse(final String response, final Assertion assertion)
            throws TicketValidationException {
    }
    public final void setProxyCallbackUrl(final String proxyCallbackUrl) {
        this.proxyCallbackUrl = proxyCallbackUrl;
    }
    public final void setProxyGrantingTicketStorage(final ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
        this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
    }
    public final void setProxyRetriever(final ProxyRetriever proxyRetriever) {
        this.proxyRetriever = proxyRetriever;
    }
    protected final String getProxyCallbackUrl() {
        return this.proxyCallbackUrl;
    }
    protected final ProxyGrantingTicketStorage getProxyGrantingTicketStorage() {
        return this.proxyGrantingTicketStorage;
    }
    protected final ProxyRetriever getProxyRetriever() {
        return this.proxyRetriever;
    }
    private class CustomAttributeHandler extends DefaultHandler {
        private Map<String, Object> attributes;
        private boolean foundAttributes;
        private String currentAttribute;
        private StringBuilder value;
        @Override
        public void startDocument() throws SAXException {
            this.attributes = new HashMap<String, Object>();
        }
        @Override
        public void startElement(final String namespaceURI, final String localName, final String qName,
                final Attributes attributes) throws SAXException {
            if ("attributes".equals(localName)) {
                this.foundAttributes = true;
            } else if (this.foundAttributes) {
                this.value = new StringBuilder();
                this.currentAttribute = localName;
            }
        }
        @Override
        public void characters(final char[] chars, final int start, final int length) throws SAXException {
            if (this.currentAttribute != null) {
                value.append(chars, start, length);
            }
        }
        @Override
        public void endElement(final String namespaceURI, final String localName, final String qName)
                throws SAXException {
            if ("attributes".equals(localName)) {
                this.foundAttributes = false;
                this.currentAttribute = null;
            } else if (this.foundAttributes) {
                final Object o = this.attributes.get(this.currentAttribute);
                if (o == null) {
                    this.attributes.put(this.currentAttribute, this.value.toString());
                } else {
                    final List<Object> items;
                    if (o instanceof List) {
                        items = (List<Object>) o;
                    } else {
                        items = new LinkedList<Object>();
                        items.add(o);
                        this.attributes.put(this.currentAttribute, items);
                    }
                    items.add(this.value.toString());
                }
            }
        }
        public Map<String, Object> getAttributes() {
            return this.attributes;
        }
    }
}
