package jenkins.model;
import hudson.Extension;
import hudson.Util;
import hudson.XmlFile;
import hudson.util.FormValidation;
import hudson.util.XStream2;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import static hudson.Util.fixNull;
@Extension
public class JenkinsLocationConfiguration extends GlobalConfiguration {
    private transient String hudsonUrl;
    private String adminAddress;
    private String jenkinsUrl;
    private transient String charset,useSsl;
    public static JenkinsLocationConfiguration get() {
        return GlobalConfiguration.all().get(JenkinsLocationConfiguration.class);
    }
    public JenkinsLocationConfiguration() {
        load();
    }
    @Override
    public synchronized void load() {
        XmlFile file = getConfigFile();
        if(!file.exists()) {
            XStream2 xs = new XStream2();
            xs.addCompatibilityAlias("hudson.tasks.Mailer$DescriptorImpl",JenkinsLocationConfiguration.class);
            file = new XmlFile(xs,new File(Jenkins.getInstance().getRootDir(),"hudson.tasks.Mailer.xml"));
            if (file.exists()) {
                try {
                    file.unmarshal(this);
                    if (jenkinsUrl==null)
                        jenkinsUrl = hudsonUrl;
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Failed to load "+file, e);
                }
            }
        } else {
            super.load();
        }
        updateSecureSessionFlag();
    }
    public String getAdminAddress() {
        String v = adminAddress;
        if(v==null)     v = Messages.Mailer_Address_Not_Configured();
        return v;
    }
    public void setAdminAddress(String adminAddress) {
        if(adminAddress.startsWith("\"") && adminAddress.endsWith("\"")) {
            adminAddress = adminAddress.substring(1,adminAddress.length()-1);
        }
        this.adminAddress = adminAddress;
        save();
    }
    public String getUrl() {
        return jenkinsUrl;
    }
    public void setUrl(String hudsonUrl) {
        String url = Util.nullify(hudsonUrl);
        if(url!=null && !url.endsWith("/"))
            url += '/';
        this.jenkinsUrl = url;
        save();
        updateSecureSessionFlag();
    }
    private void updateSecureSessionFlag() {
        try {
            ServletContext context = Jenkins.getInstance().servletContext;
            Method m;
            try {
                m = context.getClass().getMethod("getSessionCookieConfig");
            } catch (NoSuchMethodException x) { 
                LOGGER.log(Level.FINE, "Failed to set secure cookie flag", x);
                return;
            }
            Object sessionCookieConfig = m.invoke(context);
            Class scc = Class.forName("javax.servlet.SessionCookieConfig");
            Method setSecure = scc.getMethod("setSecure", boolean.class);
            boolean v = fixNull(jenkinsUrl).startsWith("https");
            setSecure.invoke(sessionCookieConfig, v);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof IllegalStateException) {
                return;
            }
            LOGGER.log(Level.WARNING, "Failed to set secure cookie flag", e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to set secure cookie flag", e);
        }
    }
    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this,json);
        return true;
    }
    public FormValidation doCheckUrl(@QueryParameter String value) {
        if(value.startsWith("http:
            return FormValidation.warning(Messages.Mailer_Localhost_Error());
        return FormValidation.ok();
    }
    public FormValidation doCheckAdminAddress(@QueryParameter String value) {
        try {
            new InternetAddress(value);
            return FormValidation.ok();
        } catch (AddressException e) {
            return FormValidation.error(e.getMessage());
        }
    }
    private static final Logger LOGGER = Logger.getLogger(JenkinsLocationConfiguration.class.getName());
}
