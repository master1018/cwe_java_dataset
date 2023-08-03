
package hudson.diagnosis;
import hudson.Extension;
import hudson.Util;
import hudson.model.AdministrativeMonitor;
import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.Stapler;
@Extension
public class ReverseProxySetupMonitor extends AdministrativeMonitor {
    private static final Logger LOGGER = Logger.getLogger(ReverseProxySetupMonitor.class.getName());
    @Override
    public boolean isActivated() {
        return true;
    }
    public HttpResponse doTest() {
        String referer = Stapler.getCurrentRequest().getReferer();
        Jenkins j = Jenkins.getInstance();
        assert j != null;
        String redirect = j.getRootUrl() + "administrativeMonitor/" + id + "/testForReverseProxySetup/" + (referer != null ? Util.rawEncode(referer) : "NO-REFERER") + "/";
        LOGGER.log(Level.FINE, "coming from {0} and redirecting to {1}", new Object[] {referer, redirect});
        return new HttpRedirect(redirect);
    }
    public void getTestForReverseProxySetup(String rest) {
        Jenkins j = Jenkins.getInstance();
        assert j != null;
        String inferred = j.getRootUrlFromRequest() + "manage";
        if (rest.startsWith(inferred)) { 
            throw HttpResponses.ok();
        } else {
            LOGGER.log(Level.WARNING, "{0} vs. {1}", new Object[] {inferred, rest});
            throw HttpResponses.errorWithoutStack(404, inferred + " vs. " + rest);
        }
    }
    public HttpResponse doAct(@QueryParameter String no) throws IOException {
        if(no!=null) { 
            disable(true);
            return HttpResponses.redirectViaContextPath("/manage");
        } else {
            return new HttpRedirect("https:
        }
    }
}
