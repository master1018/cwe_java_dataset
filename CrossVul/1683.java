
package hudson.diagnosis;
import hudson.model.AdministrativeMonitor;
import jenkins.model.Jenkins;
import hudson.model.AbstractModelObject;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.ExtensionList;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import java.io.IOException;
import java.util.List;
@Extension
public final class HudsonHomeDiskUsageMonitor extends AdministrativeMonitor {
     boolean activated;
    public HudsonHomeDiskUsageMonitor() {
        super("hudsonHomeIsFull");
    }
    public boolean isActivated() {
        return activated;
    }
    @Override
    public String getDisplayName() {
    	return Messages.HudsonHomeDiskUsageMonitor_DisplayName();
    }
    public HttpResponse doAct(@QueryParameter String no) throws IOException {
        if(no!=null) {
            disable(true);
            return HttpResponses.redirectViaContextPath("/manage");
        } else {
            return HttpResponses.redirectToDot();
        }
    }
    public List<Solution> getSolutions() {
        return Solution.all();
    }
    public Solution getSolution(String id) {
        for( Solution s : Solution.all() )
            if(s.id.equals(id))
                return s;
        return null;
    }
    public static HudsonHomeDiskUsageMonitor get() {
        return all().get(HudsonHomeDiskUsageMonitor.class);
    }
    public static abstract class Solution extends AbstractModelObject implements ExtensionPoint {
        public final String id;
        protected Solution(String id) {
            this.id = id;
        }
        protected Solution() {
            this.id = this.getClass().getName();
        }
        public String getUrl() {
            return HudsonHomeDiskUsageMonitor.get().getUrl()+"/solution/"+id;
        }
        public static ExtensionList<Solution> all() {
            return ExtensionList.lookup(Solution.class);
        }
    }
}
