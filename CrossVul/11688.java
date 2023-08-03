package jenkins.security.s2m;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.AdministrativeMonitor;
import hudson.remoting.Callable;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.interceptor.RequirePOST;
import javax.inject.Inject;
import java.io.IOException;
@Extension
public class AdminCallableMonitor extends AdministrativeMonitor {
    @Inject
    Jenkins jenkins;
    @Inject
    AdminWhitelistRule rule;
    public AdminCallableMonitor() {
        super("slaveToMasterAccessControl");
    }
    @Override
    public boolean isActivated() {
        return !rule.rejected.describe().isEmpty();
    }
    @Override
    public String getDisplayName() {
        return "Slave \u2192 Master Access Control";
    }
    public AdminWhitelistRule getRule() {
        return rule;
    }
    @RequirePOST
    public HttpResponse doAct(@QueryParameter String dismiss) throws IOException {
        if(dismiss!=null) {
            disable(true);
            return HttpResponses.redirectViaContextPath("/manage");
        } else {
            return HttpResponses.redirectTo("rule/");
        }
    }
    public HttpResponse doIndex() {
        return HttpResponses.redirectTo("rule/");
    }
}
