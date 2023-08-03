
package org.keycloak.saml.common.util;
import org.keycloak.saml.common.ErrorCodes;
import org.keycloak.saml.common.PicketLinkLogger;
import org.keycloak.saml.common.PicketLinkLoggerFactory;
import org.keycloak.saml.common.constants.GeneralConstants;
import org.keycloak.saml.common.constants.JBossSAMLConstants;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;
import org.keycloak.saml.common.exceptions.ConfigurationException;
import org.keycloak.saml.common.exceptions.ParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
public class StaxParserUtil {
    private static final PicketLinkLogger logger = PicketLinkLoggerFactory.getLogger();
    public static void validate(InputStream doc, InputStream sch) throws ParsingException {
        try {
            XMLEventReader xmlEventReader = StaxParserUtil.getXMLEventReader(doc);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(sch));
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(xmlEventReader));
        } catch (Exception e) {
            throw logger.parserException(e);
        }
    }
    public static void bypassElementBlock(XMLEventReader xmlEventReader, String tag) throws ParsingException {
        while (xmlEventReader.hasNext()) {
            EndElement endElement = getNextEndElement(xmlEventReader);
            if (endElement == null)
                return;
            if (StaxParserUtil.matches(endElement, tag))
                return;
        }
    }
    public static boolean wasWhitespacePeeked(XMLEventReader xmlEventReader, XMLEvent xmlEvent) {
        if (xmlEvent.isCharacters()) {
            Characters chars = xmlEvent.asCharacters();
            String data = chars.getData();
            if (data == null || data.trim().equals("")) {
                try {
                    xmlEventReader.nextEvent();
                    return true;
                } catch (XMLStreamException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
    public static String getAttributeValue(Attribute attribute) {
        String str = trim(attribute.getValue());
        return str;
    }
    public static String getAttributeValue(StartElement startElement, String tag) {
        String result = null;
        Attribute attr = startElement.getAttributeByName(new QName(tag));
        if (attr != null)
            result = getAttributeValue(attr);
        return result;
    }
    public static boolean getBooleanAttributeValue(StartElement startElement, String tag) {
        return getBooleanAttributeValue(startElement, tag, false);
    }
    public static boolean getBooleanAttributeValue(StartElement startElement, String tag, boolean defaultValue) {
        String result = null;
        Attribute attr = startElement.getAttributeByName(new QName(tag));
        if (attr != null)
            result = getAttributeValue(attr);
        if (result == null) return defaultValue;
        return Boolean.valueOf(result);
    }
    public static Element getDOMElement(XMLEventReader xmlEventReader) throws ParsingException {
        Transformer transformer = null;
        final String JDK_TRANSFORMER_PROPERTY = "picketlink.jdk.transformer";
        boolean useJDKTransformer = Boolean.parseBoolean(SecurityActions.getSystemProperty(JDK_TRANSFORMER_PROPERTY, "false"));
        try {
            if (useJDKTransformer) {
                transformer = TransformerUtil.getTransformer();
            } else {
                transformer = TransformerUtil.getStaxSourceToDomResultTransformer();
            }
            Document resultDocument = DocumentUtil.createDocument();
            DOMResult domResult = new DOMResult(resultDocument);
            Source source = new StAXSource(xmlEventReader);
            TransformerUtil.transform(transformer, source, domResult);
            Document doc = (Document) domResult.getNode();
            return doc.getDocumentElement();
        } catch (ConfigurationException e) {
            throw logger.parserException(e);
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }
    public static String getElementText(XMLEventReader xmlEventReader) throws ParsingException {
        String str = null;
        try {
            str = xmlEventReader.getElementText().trim();
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
        return str;
    }
    public static XMLEventReader getXMLEventReader(InputStream is) {
        XMLInputFactory xmlInputFactory;
        XMLEventReader xmlEventReader = null;
        try {
            xmlInputFactory = XML_INPUT_FACTORY.get();
            xmlEventReader = xmlInputFactory.createXMLEventReader(is);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return xmlEventReader;
    }
    public static String getLineColumnNumber(Location location) {
        StringBuilder builder = new StringBuilder("[");
        builder.append(location.getLineNumber()).append(",").append(location.getColumnNumber()).append("]");
        return builder.toString();
    }
    public static XMLEvent getNextEvent(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            return xmlEventReader.nextEvent();
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }
    public static StartElement getNextStartElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent == null || xmlEvent.isStartElement())
                    return (StartElement) xmlEvent;
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
        return null;
    }
    public static EndElement getNextEndElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent == null || xmlEvent.isEndElement())
                    return (EndElement) xmlEvent;
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
        return null;
    }
    public static String getStartElementName(StartElement startElement) {
        return trim(startElement.getName().getLocalPart());
    }
    public static String getEndElementName(EndElement endElement) {
        return trim(endElement.getName().getLocalPart());
    }
    public static String getXSITypeValue(StartElement startElement) {
        Attribute xsiType = startElement.getAttributeByName(new QName(JBossSAMLURIConstants.XSI_NSURI.get(),
                JBossSAMLConstants.TYPE.get()));
        if (xsiType == null)
            throw logger.parserExpectedXSI(ErrorCodes.EXPECTED_XSI);
        return StaxParserUtil.getAttributeValue(xsiType);
    }
    public static boolean hasTextAhead(XMLEventReader xmlEventReader) throws ParsingException {
        XMLEvent event = peek(xmlEventReader);
        return event.getEventType() == XMLEvent.CHARACTERS;
    }
    public static boolean matches(StartElement startElement, String tag) {
        String elementTag = getStartElementName(startElement);
        return tag.equals(elementTag);
    }
    public static boolean matches(EndElement endElement, String tag) {
        String elementTag = getEndElementName(endElement);
        return tag.equals(elementTag);
    }
    public static XMLEvent peek(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            return xmlEventReader.peek();
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }
    public static StartElement peekNextStartElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (true) {
                XMLEvent xmlEvent = xmlEventReader.peek();
                if (xmlEvent == null || xmlEvent.isStartElement())
                    return (StartElement) xmlEvent;
                else
                    xmlEvent = xmlEventReader.nextEvent();
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }
    public static EndElement peekNextEndElement(XMLEventReader xmlEventReader) throws ParsingException {
        try {
            while (true) {
                XMLEvent xmlEvent = xmlEventReader.peek();
                if (xmlEvent == null || xmlEvent.isEndElement())
                    return (EndElement) xmlEvent;
                else
                    xmlEvent = xmlEventReader.nextEvent();
            }
        } catch (XMLStreamException e) {
            throw logger.parserException(e);
        }
    }
    public static final String trim(String str) {
        if (str == null)
            throw logger.nullArgumentError("String to trim");
        return str.trim();
    }
    public static void validate(StartElement startElement, String tag) {
        String foundElementTag = getStartElementName(startElement);
        if (!tag.equals(foundElementTag))
            throw logger.parserExpectedTag(tag, foundElementTag);
    }
    public static void validate(EndElement endElement, String tag) {
        String elementTag = getEndElementName(endElement);
        if (!tag.equals(elementTag))
            throw new RuntimeException(logger.parserExpectedEndTag("</" + tag + ">.  Found </" + elementTag + ">"));
    }
    private static final ThreadLocal<XMLInputFactory> XML_INPUT_FACTORY = new ThreadLocal<XMLInputFactory>() {
        @Override
        protected XMLInputFactory initialValue() {
            return getXMLInputFactory();
        }
    };
    private static XMLInputFactory getXMLInputFactory() {
        boolean tccl_jaxp = SystemPropertiesUtil.getSystemProperty(GeneralConstants.TCCL_JAXP, "false")
                .equalsIgnoreCase("true");
        ClassLoader prevTCCL = SecurityActions.getTCCL();
        try {
            if (tccl_jaxp) {
                SecurityActions.setTCCL(StaxParserUtil.class.getClassLoader());
            }
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
            return xmlInputFactory;
        } finally {
            if (tccl_jaxp) {
                SecurityActions.setTCCL(prevTCCL);
            }
        }
    }
}