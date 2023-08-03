
package hudson.model;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import java.io.File;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import hudson.slaves.OfflineCause;
import jenkins.model.Jenkins;
import hudson.slaves.DumbSlave;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;
public class ComputerTest {
    @Rule public JenkinsRule j = new JenkinsRule();
    @Test
    public void discardLogsAfterDeletion() throws Exception {
        DumbSlave delete = j.createOnlineSlave(Jenkins.getInstance().getLabelAtom("delete"));
        DumbSlave keep = j.createOnlineSlave(Jenkins.getInstance().getLabelAtom("keep"));
        File logFile = delete.toComputer().getLogFile();
        assertTrue(logFile.exists());
        Jenkins.getInstance().removeNode(delete);
        assertFalse("Slave log should be deleted", logFile.exists());
        assertFalse("Slave log directory should be deleted", logFile.getParentFile().exists());
        assertTrue("Slave log should be kept", keep.toComputer().getLogFile().exists());
    }
    @Test
    public void doNotShowUserDetailsInOfflineCause() throws Exception {
        DumbSlave slave = j.createOnlineSlave();
        final Computer computer = slave.toComputer();
        computer.setTemporarilyOffline(true, new OfflineCause.UserCause(User.get("username"), "msg"));
        verifyOfflineCause(computer);
    }
    @Test @LocalData
    public void removeUserDetailsFromOfflineCause() throws Exception {
        Computer computer = j.jenkins.getComputer("deserialized");
        verifyOfflineCause(computer);
    }
    private void verifyOfflineCause(Computer computer) throws Exception {
        XmlPage page = j.createWebClient().goToXml("computer/" + computer.getName() + "/config.xml");
        String content = page.getWebResponse().getContentAsString("UTF-8");
        assertThat(content, containsString("temporaryOfflineCause"));
        assertThat(content, containsString("<userId>username</userId>"));
        assertThat(content, not(containsString("ApiTokenProperty")));
        assertThat(content, not(containsString("apiToken")));
    }
}
