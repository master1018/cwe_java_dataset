
package net.sf.mpxj.common;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
public final class UnmarshalHelper
{
   public static final Object unmarshal(JAXBContext context, InputStream stream) throws JAXBException, SAXException, ParserConfigurationException
   {
      return context.createUnmarshaller().unmarshal(new SAXSource(XmlReaderHelper.createXmlReader(), new InputSource(stream)));
   }
   public static final Object unmarshal(JAXBContext context, InputStream stream, XMLFilter filter) throws JAXBException, SAXException, ParserConfigurationException, IOException
   {
      return unmarshal(context, new InputSource(stream), filter, false);
   }
   public static final Object unmarshal(JAXBContext context, InputSource source, XMLFilter filter, boolean ignoreValidationErrors) throws JAXBException, SAXException, ParserConfigurationException, IOException
   {
      Unmarshaller unmarshaller = context.createUnmarshaller();
      if (ignoreValidationErrors)
      {
         unmarshaller.setEventHandler(new ValidationEventHandler()
         {
            @Override public boolean handleEvent(ValidationEvent event)
            {
               return true;
            }
         });
      }
      UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
      filter.setParent(XmlReaderHelper.createXmlReader());
      filter.setContentHandler(unmarshallerHandler);
      filter.parse(source);
      return unmarshallerHandler.getResult();
   }
}
