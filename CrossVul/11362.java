
package com.thoughtworks.acceptance;
import java.beans.EventHandler;
import java.util.Iterator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.ForbiddenClassException;
public class SecurityVulnerabilityTest extends AbstractAcceptanceTest {
    private final static StringBuffer BUFFER = new StringBuffer();
    protected void setUp() throws Exception {
        super.setUp();
        BUFFER.setLength(0);
        xstream.alias("runnable", Runnable.class);
    }
    protected void setupSecurity(XStream xstream) {
    }
    public void testCannotInjectEventHandler() {
        final String xml = ""
            + "<string class='runnable-array'>\n"
            + "  <dynamic-proxy>\n"
            + "    <interface>java.lang.Runnable</interface>\n"
            + "    <handler class='java.beans.EventHandler'>\n"
            + "      <target class='com.thoughtworks.acceptance.SecurityVulnerabilityTest$Exec'/>\n"
            + "      <action>exec</action>\n"
            + "    </handler>\n"
            + "  </dynamic-proxy>\n"
            + "</string>";
        try {
            xstream.fromXML(xml);
            fail("Thrown " + XStreamException.class.getName() + " expected");
        } catch (final XStreamException e) {
            assertTrue(e.getMessage().indexOf(EventHandler.class.getName()) > 0);
        }
        assertEquals(0, BUFFER.length());
    }
    public void testCannotInjectEventHandlerWithUnconfiguredSecurityFramework() {
        xstream.alias("runnable", Runnable.class);
        final String xml = ""
            + "<string class='runnable-array'>\n"
            + "  <dynamic-proxy>\n"
            + "    <interface>java.lang.Runnable</interface>\n"
            + "    <handler class='java.beans.EventHandler'>\n"
            + "      <target class='com.thoughtworks.acceptance.SecurityVulnerabilityTest$Exec'/>\n"
            + "      <action>exec</action>\n"
            + "    </handler>\n"
            + "  </dynamic-proxy>\n"
            + "</string>";
        try {
            xstream.fromXML(xml);
            fail("Thrown " + XStreamException.class.getName() + " expected");
        } catch (final XStreamException e) {
            assertTrue(e.getMessage().indexOf(EventHandler.class.getName()) >= 0);
        }
        assertEquals(0, BUFFER.length());
    }
    public void testExplicitlyConvertEventHandler() {
        final String xml = ""
            + "<string class='runnable-array'>\n"
            + "  <dynamic-proxy>\n"
            + "    <interface>java.lang.Runnable</interface>\n"
            + "    <handler class='java.beans.EventHandler'>\n"
            + "      <target class='com.thoughtworks.acceptance.SecurityVulnerabilityTest$Exec'/>\n"
            + "      <action>exec</action>\n"
            + "    </handler>\n"
            + "  </dynamic-proxy>\n"
            + "</string>";
        xstream.allowTypes(new Class[]{EventHandler.class});
        final Runnable[] array = (Runnable[])xstream.fromXML(xml);
        assertEquals(0, BUFFER.length());
        array[0].run();
        assertEquals("Executed!", BUFFER.toString());
    }
    public void testCannotInjectConvertImageIOContainsFilterWithUnconfiguredSecurityFramework() {
        if (JVM.isVersion(7)) {
            final String xml = ""
                + "<string class='javax.imageio.spi.FilterIterator'>\n"
                + " <iter class='java.util.ArrayList$Itr'>\n"
                + "   <cursor>0</cursor>\n"
                + "   <lastRet>1</lastRet>\n"
                + "   <expectedModCount>1</expectedModCount>\n"
                + "   <outer-class>\n"
                + "     <com.thoughtworks.acceptance.SecurityVulnerabilityTest_-Exec/>\n"
                + "   </outer-class>\n"
                + " </iter>\n"
                + " <filter class='javax.imageio.ImageIO$ContainsFilter'>\n"
                + "   <method>\n"
                + "     <class>com.thoughtworks.acceptance.SecurityVulnerabilityTest$Exec</class>\n"
                + "     <name>exec</name>\n"
                + "     <parameter-types/>\n"
                + "   </method>\n"
                + "   <name>exec</name>\n"
                + " </filter>\n"
                + " <next/>\n"
                + "</string>";
            try {
                xstream.fromXML(xml);
                fail("Thrown " + XStreamException.class.getName() + " expected");
            } catch (final XStreamException e) {
                assertTrue(e.getMessage().indexOf("javax.imageio.ImageIO$ContainsFilter") >= 0);
            }
            assertEquals(0, BUFFER.length());
        }
    }
    public void testExplicitlyConvertImageIOContainsFilter() {
        if (JVM.isVersion(7)) {
            final String xml = ""
                + "<string class='javax.imageio.spi.FilterIterator'>\n"
                + " <iter class='java.util.ArrayList$Itr'>\n"
                + "   <cursor>0</cursor>\n"
                + "   <lastRet>1</lastRet>\n"
                + "   <expectedModCount>1</expectedModCount>\n"
                + "   <outer-class>\n"
                + "     <com.thoughtworks.acceptance.SecurityVulnerabilityTest_-Exec/>\n"
                + "   </outer-class>\n"
                + " </iter>\n"
                + " <filter class='javax.imageio.ImageIO$ContainsFilter'>\n"
                + "   <method>\n"
                + "     <class>com.thoughtworks.acceptance.SecurityVulnerabilityTest$Exec</class>\n"
                + "     <name>exec</name>\n"
                + "     <parameter-types/>\n"
                + "   </method>\n"
                + "   <name>exec</name>\n"
                + " </filter>\n"
                + " <next/>\n"
                + "</string>";
            xstream.allowTypes(new String[]{"javax.imageio.ImageIO$ContainsFilter"});
            final Iterator iterator = (Iterator)xstream.fromXML(xml);
            assertEquals(0, BUFFER.length());
            iterator.next();
            assertEquals("Executed!", BUFFER.toString());
        }
    }
    public static class Exec {
        public void exec() {
            BUFFER.append("Executed!");
        }
    }
    public void testInstanceOfVoid() {
        try {
            xstream.fromXML("<void/>");
            fail("Thrown " + ConversionException.class.getName() + " expected");
        } catch (final ConversionException e) {
            assertEquals("void", e.get("construction-type"));
        }
    }
    public void testDeniedInstanceOfVoid() {
        xstream.addPermission(AnyTypePermission.ANY); 
        xstream.denyTypes(new Class[]{void.class, Void.class});
        try {
            xstream.fromXML("<void/>");
            fail("Thrown " + ForbiddenClassException.class.getName() + " expected");
        } catch (final ForbiddenClassException e) {
        }
    }
    public void testAllowedInstanceOfVoid() {
        xstream.allowTypes(new Class[]{void.class, Void.class});
        try {
            xstream.fromXML("<void/>");
            fail("Thrown " + ConversionException.class.getName() + " expected");
        } catch (final ConversionException e) {
            assertEquals("void", e.get("construction-type"));
        }
    }
    public static class LazyIterator {}
    public void testInstanceOfLazyIterator() {
        xstream.alias("lazy-iterator", LazyIterator.class);
        try {
            xstream.fromXML("<lazy-iterator/>");
            fail("Thrown " + ForbiddenClassException.class.getName() + " expected");
        } catch (final ForbiddenClassException e) {
        }
    }
}
