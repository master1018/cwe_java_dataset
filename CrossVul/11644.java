
package org.postgresql.xml;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
public class DefaultPGXmlFactoryFactory implements PGXmlFactoryFactory {
  public static final DefaultPGXmlFactoryFactory INSTANCE = new DefaultPGXmlFactoryFactory();
  private DefaultPGXmlFactoryFactory() {
  }
  private DocumentBuilderFactory getDocumentBuilderFactory() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    setFactoryProperties(factory);
    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    return factory;
  }
  @Override
  public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
    DocumentBuilder builder = getDocumentBuilderFactory().newDocumentBuilder();
    builder.setEntityResolver(EmptyStringEntityResolver.INSTANCE);
    builder.setErrorHandler(NullErrorHandler.INSTANCE);
    return builder;
  }
  @Override
  public TransformerFactory newTransformerFactory() {
    TransformerFactory factory = TransformerFactory.newInstance();
    setFactoryProperties(factory);
    return factory;
  }
  @Override
  public SAXTransformerFactory newSAXTransformerFactory() {
    SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    setFactoryProperties(factory);
    return factory;
  }
  @Override
  public XMLInputFactory newXMLInputFactory() {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    setPropertyQuietly(factory, XMLInputFactory.SUPPORT_DTD, false);
    setPropertyQuietly(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    return factory;
  }
  @Override
  public XMLOutputFactory newXMLOutputFactory() {
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    return factory;
  }
  @Override
  public XMLReader createXMLReader() throws SAXException {
    XMLReader factory = XMLReaderFactory.createXMLReader();
    setFeatureQuietly(factory, "http:
    setFeatureQuietly(factory, "http:
    setFeatureQuietly(factory, "http:
    setFeatureQuietly(factory, "http:
    factory.setErrorHandler(NullErrorHandler.INSTANCE);
    return factory;
  }
  private static void setFeatureQuietly(Object factory, String name, boolean value) {
    try {
      if (factory instanceof DocumentBuilderFactory) {
        ((DocumentBuilderFactory) factory).setFeature(name, value);
      } else if (factory instanceof TransformerFactory) {
        ((TransformerFactory) factory).setFeature(name, value);
      } else if (factory instanceof XMLReader) {
        ((XMLReader) factory).setFeature(name, value);
      } else {
        throw new Error("Invalid factory class: " + factory.getClass());
      }
      return;
    } catch (Exception ignore) {
    }
  }
  private static void setAttributeQuietly(Object factory, String name, Object value) {
    try {
      if (factory instanceof DocumentBuilderFactory) {
        ((DocumentBuilderFactory) factory).setAttribute(name, value);
      } else if (factory instanceof TransformerFactory) {
        ((TransformerFactory) factory).setAttribute(name, value);
      } else {
        throw new Error("Invalid factory class: " + factory.getClass());
      }
    } catch (Exception ignore) {
    }
  }
  private static void setFactoryProperties(Object factory) {
    setFeatureQuietly(factory, XMLConstants.FEATURE_SECURE_PROCESSING, true);
    setFeatureQuietly(factory, "http:
    setFeatureQuietly(factory, "http:
    setFeatureQuietly(factory, "http:
    setFeatureQuietly(factory, "http:
    setAttributeQuietly(factory, "http:
    setAttributeQuietly(factory, "http:
    setAttributeQuietly(factory, "http:
  }
  private static void setPropertyQuietly(Object factory, String name, Object value) {
    try {
      if (factory instanceof XMLReader) {
        ((XMLReader) factory).setProperty(name, value);
      } else if (factory instanceof XMLInputFactory) {
        ((XMLInputFactory) factory).setProperty(name, value);
      } else {
        throw new Error("Invalid factory class: " + factory.getClass());
      }
    } catch (Exception ignore) {
    }
  }
}
