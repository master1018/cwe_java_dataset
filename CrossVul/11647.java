
package net.sf.mpxj.common;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
public final class XmlReaderHelper
{
   public static final XMLReader createXmlReader() throws SAXException, ParserConfigurationException
   {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setFeature("http:
      factory.setNamespaceAware(true);
      SAXParser saxParser = factory.newSAXParser();
      return saxParser.getXMLReader();
   }
}
