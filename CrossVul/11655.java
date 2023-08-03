
package io.milton.http.webdav;
import io.milton.common.StreamUtils;
import java.io.ByteArrayInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
public class DefaultPropFindRequestFieldParser implements PropFindRequestFieldParser {
    private static final Logger log = LoggerFactory.getLogger( DefaultPropFindRequestFieldParser.class );
    public DefaultPropFindRequestFieldParser() {
    }
	@Override
    public PropertiesRequest getRequestedFields( InputStream in ) {
		final Set<QName> set = new LinkedHashSet<QName>();
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            StreamUtils.readTo( in, bout, false, true );
            byte[] arr = bout.toByteArray();
            if( arr.length > 1 ) {
                ByteArrayInputStream bin = new ByteArrayInputStream( arr );
                XMLReader reader = XMLReaderFactory.createXMLReader();
				reader.setFeature("http:
				reader.setFeature("http:
				reader.setFeature("http:
                PropFindSaxHandler handler = new PropFindSaxHandler();
                reader.setContentHandler( handler );
                try {
                    reader.parse( new InputSource( bin ) );
                    if( handler.isAllProp() ) {
                        return new PropertiesRequest();
                    } else {
                        set.addAll( handler.getAttributes().keySet() );
                    }
                } catch( IOException e ) {
                    log.warn( "exception parsing request body", e );
                } catch( SAXException e ) {
                    log.warn( "exception parsing request body", e );
                }
            }
        } catch( Exception ex ) {
			log.warn("Exception parsing PROPFIND request fields. Returning empty property set", ex);
        }
		return PropertiesRequest.toProperties(set);
    }
}
