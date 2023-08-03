
package net.bull.javamelody;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.bull.javamelody.internal.common.LOG;
public class PayloadNameRequestWrapper extends HttpServletRequestWrapper {
	private static final Pattern GWT_RPC_SEPARATOR_CHAR_PATTERN = Pattern
			.compile(Pattern.quote("|"));
	private String name;
	private String requestType;
	private BufferedInputStream bufferedInputStream;
	private ServletInputStream inputStream;
	private BufferedReader reader;
	public PayloadNameRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	protected void initialize() throws IOException {
		name = null;
		requestType = null;
		final HttpServletRequest request = (HttpServletRequest) getRequest();
		final String contentType = request.getContentType();
		if (contentType == null) {
			return;
		}
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return;
		}
		try {
			if (contentType.startsWith("text/x-gwt-rpc")) {
				name = parseGwtRpcMethodName(getBufferedInputStream(), getCharacterEncoding());
				requestType = "GWT-RPC";
			} else if (contentType.startsWith("application/soap+xml") 
					|| contentType.startsWith("text/xml") 
							&& request.getHeader("SOAPAction") != null) {
				name = parseSoapMethodName(getBufferedInputStream(), getCharacterEncoding());
				requestType = "SOAP";
			} else {
				name = null;
				requestType = null;
			}
		} catch (final Exception e) {
			LOG.debug("Error trying to parse payload content for request name", e);
			name = null;
			requestType = null;
		} finally {
			resetBufferedInputStream();
		}
	}
	protected BufferedInputStream getBufferedInputStream() throws IOException {
		if (bufferedInputStream == null) {
			final ServletRequest request = getRequest();
			request.getParameterMap();
			bufferedInputStream = new BufferedInputStream(request.getInputStream());
			bufferedInputStream.mark(Integer.MAX_VALUE);
		}
		return bufferedInputStream;
	}
	protected void resetBufferedInputStream() throws IOException {
		if (bufferedInputStream != null) {
			bufferedInputStream.reset();
		}
	}
	@SuppressWarnings("resource")
	private static String parseGwtRpcMethodName(InputStream stream, String charEncoding) {
		try {
			final Scanner scanner;
			if (charEncoding == null) {
				scanner = new Scanner(stream);
			} else {
				scanner = new Scanner(stream, charEncoding);
			}
			scanner.useDelimiter(GWT_RPC_SEPARATOR_CHAR_PATTERN); 
			scanner.next(); 
			scanner.next(); 
			scanner.next(); 
			scanner.next(); 
			scanner.next(); 
			scanner.next(); 
			return "." + scanner.next(); 
		} catch (final NoSuchElementException e) {
			LOG.debug("Unable to parse GWT-RPC request", e);
			return null;
		}
	}
	static boolean scanForChildTag(XMLStreamReader reader, String tagName)
			throws XMLStreamException {
		assert reader.isStartElement();
		int level = -1;
		while (reader.hasNext()) {
			if (reader.isStartElement()) {
				level++;
			} else if (reader.isEndElement()) {
				level--;
			}
			if (level < 0) {
				break;
			}
			reader.next();
			if (level == 0 && reader.isStartElement() && reader.getLocalName().equals(tagName)) {
				return true; 
			}
		}
		return false; 
	}
	private static String parseSoapMethodName(InputStream stream, String charEncoding) {
		try {
			final XMLInputFactory factory = XMLInputFactory.newInstance();
			final XMLStreamReader xmlReader;
			if (charEncoding != null) {
				xmlReader = factory.createXMLStreamReader(stream, charEncoding);
			} else {
				xmlReader = factory.createXMLStreamReader(stream);
			}
			xmlReader.nextTag();
			if (!"Envelope".equals(xmlReader.getLocalName())) {
				LOG.debug("Unexpected first tag of SOAP request: '" + xmlReader.getLocalName()
						+ "' (expected 'Envelope')");
				return null; 
			}
			if (!scanForChildTag(xmlReader, "Body")) {
				LOG.debug("Unable to find SOAP 'Body' tag");
				return null; 
			}
			xmlReader.nextTag();
			return "." + xmlReader.getLocalName();
		} catch (final XMLStreamException e) {
			LOG.debug("Unable to parse SOAP request", e);
			return null;
		}
	}
	@Override
	public BufferedReader getReader() throws IOException {
		if (bufferedInputStream == null) {
			return super.getReader();
		}
		if (reader == null) {
			final String characterEncoding = this.getCharacterEncoding();
			if (characterEncoding == null) {
				reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
			} else {
				reader = new BufferedReader(
						new InputStreamReader(this.getInputStream(), characterEncoding));
			}
		}
		return reader;
	}
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ServletInputStream requestInputStream = super.getInputStream();
		if (bufferedInputStream == null) {
			return requestInputStream;
		}
		if (inputStream == null) {
			final BufferedInputStream myBufferedInputStream = bufferedInputStream;
			inputStream = new ServletInputStream() {
				@Override
				public int read() throws IOException {
					return myBufferedInputStream.read();
				}
				@Override
				public boolean isFinished() {
					return requestInputStream.isFinished();
				}
				@Override
				public boolean isReady() {
					return requestInputStream.isReady();
				}
				@Override
				public void setReadListener(ReadListener readListener) {
					requestInputStream.setReadListener(readListener);
				}
			};
		}
		return inputStream;
	}
	public String getPayloadRequestName() {
		return name;
	}
	public String getPayloadRequestType() {
		return requestType;
	}
}
