
package org.springframework.web.util;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
public class JavaScriptUtilsTests {
	@Test
	public void escape() {
		StringBuilder sb = new StringBuilder();
		sb.append('"');
		sb.append("'");
		sb.append("\\");
		sb.append("/");
		sb.append("\t");
		sb.append("\n");
		sb.append("\r");
		sb.append("\f");
		sb.append("\b");
		sb.append("\013");
		assertEquals("\\\"\\'\\\\\\/\\t\\n\\n\\f\\b\\v", JavaScriptUtils.javaScriptEscape(sb.toString()));
	}
	@Test
	public void escapePsLsLineTerminators() {
		StringBuilder sb = new StringBuilder();
		sb.append('\u2028');
		sb.append('\u2029');
		String result = JavaScriptUtils.javaScriptEscape(sb.toString());
		assertEquals("\\u2028\\u2029", result);
	}
	@Test
	public void escapeLessThanGreaterThanSigns() throws UnsupportedEncodingException {
		assertEquals("\\u003C\\u003E", JavaScriptUtils.javaScriptEscape("<>"));
	}
}
