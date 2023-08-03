
package org.jasig.cas.client.validation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
}
