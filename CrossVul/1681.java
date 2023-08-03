
package hudson.diagnosis;
import hudson.model.AdministrativeMonitor;
import jenkins.model.Jenkins;
import hudson.Extension;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import java.io.IOException;
@Extension
public class TooManyJobsButNoView extends AdministrativeMonitor {
    public boolean isActivated() {
        Jenkins h = Jenkins.getInstance();
        return h.getViews().size()==1 && h.getItemMap().size()> THRESHOLD;
    }
    public void doAct(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if(req.hasParameter("no")) {
            disable(true);
            rsp.sendRedirect(req.getContextPath()+"/manage");
        } else {
            rsp.sendRedirect(req.getContextPath()+"/newView");
        }
    }
    public static final int THRESHOLD = 16;
}
