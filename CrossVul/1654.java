
package io.milton.http.webdav;
import io.milton.common.ReadingException;
import io.milton.common.StreamUtils;
import io.milton.common.WritingException;
import java.io.ByteArrayInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
public class DefaultPropPatchParser implements PropPatchRequestParser {
    private final static Logger log = LoggerFactory.getLogger( DefaultPropPatchParser.class );
	@Override
    public PropPatchParseResult getRequestedFields( InputStream in ) {
        log.debug( "getRequestedFields" );
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            StreamUtils.readTo( in, bout, false, true );
            byte[] arr = bout.toByteArray();
            return parseContent( arr );
        } catch( SAXException ex ) {
            throw new RuntimeException( ex );
        } catch( ReadingException ex ) {
            throw new RuntimeException( ex );
        } catch( WritingException ex ) {
            throw new RuntimeException( ex );
        } catch( IOException ex ) {
            throw new RuntimeException( ex );
        }
    }
    private PropPatchParseResult parseContent( byte[] arr ) throws IOException, SAXException {
        if( arr.length > 0 ) {
            log.debug( "processing content" );
            ByteArrayInputStream bin = new ByteArrayInputStream( arr );
            XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setFeature("http:
            PropPatchSaxHandler handler = new PropPatchSaxHandler();
            reader.setContentHandler( handler );
            reader.parse( new InputSource( bin ) );
            log.debug( "toset: " + handler.getAttributesToSet().size());
            return new PropPatchParseResult( handler.getAttributesToSet(), handler.getAttributesToRemove().keySet() );
        } else {
            log.debug( "empty content" );
            return new PropPatchParseResult( new HashMap<QName, String>(), new HashSet<QName>() );
        }
    }
}
