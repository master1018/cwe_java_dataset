
package hudson.model;
import hudson.ExtensionPoint;
import hudson.ExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint.LegacyInstancesAreScopedToHudson;
import hudson.triggers.SCMTrigger;
import hudson.triggers.TimerTrigger;
import java.util.Set;
import java.io.IOException;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
@LegacyInstancesAreScopedToHudson
public abstract class AdministrativeMonitor extends AbstractModelObject implements ExtensionPoint {
    public final String id;
    protected AdministrativeMonitor(String id) {
        this.id = id;
    }
    protected AdministrativeMonitor() {
        this.id = this.getClass().getName();
    }
    public String getUrl() {
        return "administrativeMonitor/"+id;
    }
    public String getDisplayName() {
        return id;
    }
    public final String getSearchUrl() {
        return getUrl();
    }
    public void disable(boolean value) throws IOException {
        AbstractCIBase hudson = Jenkins.getInstance();
        Set<String> set = hudson.disabledAdministrativeMonitors;
        if(value)   set.add(id);
        else        set.remove(id);
        hudson.save();
    }
    public boolean isEnabled() {
        return !((AbstractCIBase)Jenkins.getInstance()).disabledAdministrativeMonitors.contains(id);
    }
    public abstract boolean isActivated();
    public void doDisable(StaplerRequest req, StaplerResponse rsp) throws IOException {
        Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);
        disable(true);
        rsp.sendRedirect2(req.getContextPath()+"/manage");
    }
    public static ExtensionList<AdministrativeMonitor> all() {
        return ExtensionList.lookup(AdministrativeMonitor.class);
    }
}
