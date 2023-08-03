
package hudson.model;
import static org.junit.Assert.*;
import java.io.File;
import jenkins.model.Jenkins;
import hudson.slaves.DumbSlave;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
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
}
