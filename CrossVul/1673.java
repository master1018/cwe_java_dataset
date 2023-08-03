package jenkins.security.s2m;
import hudson.Extension;
import hudson.model.AdministrativeMonitor;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import javax.inject.Inject;
import java.io.IOException;
@Extension
public class MasterKillSwitchWarning extends AdministrativeMonitor {
    @Inject
    AdminWhitelistRule rule;
    @Inject
    MasterKillSwitchConfiguration config;
    @Override
    public boolean isActivated() {
        return rule.getMasterKillSwitch() && config.isRelevant();
    }
    public HttpResponse doAct(@QueryParameter String dismiss) throws IOException {
        if(dismiss!=null) {
            disable(true);
            return HttpResponses.redirectViaContextPath("/manage");
        } else {
            return HttpResponses.redirectViaContextPath("configureSecurity");
        }
    }
}
