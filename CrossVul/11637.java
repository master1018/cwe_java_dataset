
package org.apache.activemq.artemis.selector.filter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
public class XalanXPathEvaluator implements XPathExpression.XPathEvaluator
{
   private final String xpath;
   public XalanXPathEvaluator(String xpath)
   {
      this.xpath = xpath;
   }
   public boolean evaluate(Filterable m) throws FilterException
   {
      String stringBody = m.getBodyAs(String.class);
      if (stringBody != null)
      {
         return evaluate(stringBody);
      }
      return false;
   }
   protected boolean evaluate(String text)
   {
      return evaluate(new InputSource(new StringReader(text)));
   }
   protected boolean evaluate(InputSource inputSource)
   {
      try
      {
         DocumentBuilder dbuilder = createDocumentBuilder();
         Document doc = dbuilder.parse(inputSource);
         CachedXPathAPI cachedXPathAPI = new CachedXPathAPI();
         XObject result = cachedXPathAPI.eval(doc, xpath);
         if (result.bool())
            return true;
         else
         {
            NodeIterator iterator = cachedXPathAPI.selectNodeIterator(doc, xpath);
            return (iterator.nextNode() != null);
         }
      }
      catch (Throwable e)
      {
         return false;
      }
   }
   private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException
   {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setFeature("http:
      factory.setFeature("http:
      factory.setFeature("http:
      return factory.newDocumentBuilder();
   }
}
