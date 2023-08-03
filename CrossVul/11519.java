
package org.jasig.cas.client.validation;
import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import org.jasig.cas.client.PublicTestHttpServer;
import org.junit.Before;
import org.junit.Test;
public final class Cas10TicketValidatorTests extends AbstractTicketValidatorTests {
    private static final PublicTestHttpServer server = PublicTestHttpServer.instance(8090);
    private Cas10TicketValidator ticketValidator;
    public Cas10TicketValidatorTests() {
        super();
    }
    @Before
    public void setUp() throws Exception {
        this.ticketValidator = new Cas10TicketValidator(CONST_CAS_SERVER_URL_PREFIX + "8090");
    }
    @Test
    public void testNoResponse() throws Exception {
        server.content = "no\n\n".getBytes(server.encoding);
        try {
            this.ticketValidator.validate("testTicket", "myService");
            fail("ValidationException expected.");
        } catch (final TicketValidationException e) {
        }
    }
    @Test
    public void testYesResponse() throws TicketValidationException, UnsupportedEncodingException {
        server.content = "yes\nusername\n\n".getBytes(server.encoding);
        final Assertion assertion = this.ticketValidator.validate("testTicket", "myService");
        assertEquals(CONST_USERNAME, assertion.getPrincipal().getName());
    }
    @Test
    public void testBadResponse() throws UnsupportedEncodingException {
        server.content = "falalala\n\n".getBytes(server.encoding);
        try {
            this.ticketValidator.validate("testTicket", "myService");
            fail("ValidationException expected.");
        } catch (final TicketValidationException e) {
        }
    }
    @Test
    public void urlEncodedValues() {
        final String ticket = "ST-1-owKEOtYJjg77iHcCQpkl-cas01.example.org%26%73%65%72%76%69%63%65%3d%68%74%74%70%25%33%41%25%32%46%25%32%46%31%32%37%2e%30%2e%30%2e%31%25%32%46%62%6f%72%69%6e%67%25%32%46%23";
        final String service = "foobar";
        final String url = this.ticketValidator.constructValidationUrl(ticket, service);
        final String encodedValue = this.ticketValidator.encodeUrl(ticket);
        assertTrue(url.contains(encodedValue));
        assertFalse(url.contains(ticket));
    }
}
