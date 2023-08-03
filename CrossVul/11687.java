package jenkins.security;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.TaskListener;
import hudson.util.HttpResponses;
import hudson.util.SecretRewriter;
import hudson.util.VersionNumber;
import jenkins.management.AsynchronousAdministrativeMonitor;
import jenkins.model.Jenkins;
import jenkins.util.io.FileBoolean;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.interceptor.RequirePOST;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
@Extension
public class RekeySecretAdminMonitor extends AsynchronousAdministrativeMonitor {
    private final FileBoolean needed = state("needed");
    private final FileBoolean done = state("done");
    private final FileBoolean scanOnBoot = state("scanOnBoot");
    public RekeySecretAdminMonitor() throws IOException {
        Jenkins j = Jenkins.getInstance();
        if (j.isUpgradedFromBefore(new VersionNumber("1.496.*"))
        &&  new FileBoolean(new File(j.getRootDir(),"secret.key.not-so-secret")).isOff())
            needed.on();
    }
    @Override
    public boolean isActivated() {
        return needed.isOn();
    }
    public boolean isDone() {
        return done.isOn();
    }
    public void setNeeded() {
        needed.on();
    }
    public boolean isScanOnBoot() {
        return scanOnBoot.isOn();
    }
    @RequirePOST
    public HttpResponse doScan(StaplerRequest req) throws IOException, GeneralSecurityException {
        if(req.hasParameter("background")) {
            start(false);
        } else
        if(req.hasParameter("schedule")) {
            scanOnBoot.on();
        } else
        if(req.hasParameter("dismiss")) {
            disable(true);
        } else
            throw HttpResponses.error(400,"Invalid request submission: " + req.getParameterMap());
        return HttpResponses.redirectViaContextPath("/manage");
    }
    private FileBoolean state(String name) {
        return new FileBoolean(new File(getBaseDir(),name));
    }
    @Initializer(fatal=false,after=InitMilestone.PLUGINS_STARTED,before=InitMilestone.EXTENSIONS_AUGMENTED)
    public static void scanOnReboot() throws InterruptedException, IOException, GeneralSecurityException {
        RekeySecretAdminMonitor m = new RekeySecretAdminMonitor();  
        FileBoolean flag = m.scanOnBoot;
        if (flag.isOn()) {
            flag.off();
            m.start(false).join();
        }
    }
    @Override
    public String getDisplayName() {
        return Messages.RekeySecretAdminMonitor_DisplayName();
    }
    @Override
    protected File getLogFile() {
        return new File(getBaseDir(),"rekey.log");
    }
    @Override
    protected void fix(TaskListener listener) throws Exception {
        LOGGER.info("Initiating a re-keying of secrets. See "+getLogFile());
        SecretRewriter rewriter = new SecretRewriter(new File(getBaseDir(),"backups"));
        try {
            PrintStream log = listener.getLogger();
            log.println("Started re-keying " + new Date());
            int count = rewriter.rewriteRecursive(Jenkins.getInstance().getRootDir(), listener);
            log.printf("Completed re-keying %d files on %s\n",count,new Date());
            new RekeySecretAdminMonitor().done.on();
            LOGGER.info("Secret re-keying completed");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fatal failure in re-keying secrets",e);
            e.printStackTrace(listener.error("Fatal failure in rewriting secrets"));
        }
    }
    private static final Logger LOGGER = Logger.getLogger(RekeySecretAdminMonitor.class.getName());
}
