
package org.postgresql.xml;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
public class EmptyStringEntityResolver implements EntityResolver {
  public static final EmptyStringEntityResolver INSTANCE = new EmptyStringEntityResolver();
  @Override
  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException {
    return new InputSource(new StringReader(""));
  }
}
