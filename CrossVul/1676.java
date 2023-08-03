package jenkins.diagnostics;
import hudson.Extension;
import hudson.model.AdministrativeMonitor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import java.io.IOException;
@Extension
public class SecurityIsOffMonitor extends AdministrativeMonitor {
    @Override
    public boolean isActivated() {
        return !Jenkins.getInstance().isUseSecurity();
    }
    public void doAct(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if(req.hasParameter("no")) {
            disable(true);
            rsp.sendRedirect(req.getContextPath()+"/manage");
        } else {
            rsp.sendRedirect(req.getContextPath()+"/configureSecurity");
        }
    }
}
