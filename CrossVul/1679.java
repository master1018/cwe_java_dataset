package hudson.diagnosis;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.io.IOException;
public class HudsonHomeDiskUsageMonitorTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();
    @Test
    public void flow() throws Exception {
        HudsonHomeDiskUsageMonitor mon = HudsonHomeDiskUsageMonitor.get();
        mon.activated = true;
        j.submit(getForm(mon), "yes");
        assertTrue(mon.isEnabled());
        mon.doAct("no");
        assertFalse(mon.isEnabled());
        try {
            fail(getForm(mon)+" shouldn't be there");
        } catch (ElementNotFoundException e) {
        }
    }
    private HtmlForm getForm(HudsonHomeDiskUsageMonitor mon) throws IOException, SAXException {
        HtmlPage p = j.createWebClient().goTo("manage");
        return p.getFormByName(mon.id);
    }
}
