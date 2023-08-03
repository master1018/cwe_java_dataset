
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
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.StaplerProxy;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;
@LegacyInstancesAreScopedToHudson
public abstract class AdministrativeMonitor extends AbstractModelObject implements ExtensionPoint, StaplerProxy {
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
    @RequirePOST
    public void doDisable(StaplerRequest req, StaplerResponse rsp) throws IOException {
        disable(true);
        rsp.sendRedirect2(req.getContextPath()+"/manage");
    }
    @Restricted(NoExternalUse.class)
    public Object getTarget() {
        Jenkins.getInstance().checkPermission(Jenkins.ADMINISTER);
        return this;
    }
    public static ExtensionList<AdministrativeMonitor> all() {
        return ExtensionList.lookup(AdministrativeMonitor.class);
    }
}
