
package hudson.model;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.util.Secret;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
public class PasswordParameterDefinition extends SimpleParameterDefinition {
    private Secret defaultValue;
    @DataBoundConstructor
    public PasswordParameterDefinition(String name, String defaultValue, String description) {
        super(name, description);
        this.defaultValue = Secret.fromString(defaultValue);
    }
    @Override
    public ParameterDefinition copyWithDefaultValue(ParameterValue defaultValue) {
        if (defaultValue instanceof PasswordParameterValue) {
            PasswordParameterValue value = (PasswordParameterValue) defaultValue;
            return new PasswordParameterDefinition(getName(), Secret.toString(value.getValue()), getDescription());
        } else {
            return this;
        }
    }
    @Override
    public ParameterValue createValue(String value) {
        return new PasswordParameterValue(getName(), value, getDescription());
    }
    @Override
    public PasswordParameterValue createValue(StaplerRequest req, JSONObject jo) {
        PasswordParameterValue value = req.bindJSON(PasswordParameterValue.class, jo);
        value.setDescription(getDescription());
        return value;
    }
    @Override
    public ParameterValue getDefaultParameterValue() {
        return new PasswordParameterValue(getName(), getDefaultValue(), getDescription());
    }
    public String getDefaultValue() {
        return Secret.toString(defaultValue);
    }
    @Restricted(DoNotUse.class) 
    public Secret getDefaultValueAsSecret() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = Secret.fromString(defaultValue);
    }
    @Extension
    public final static class ParameterDescriptorImpl extends ParameterDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.PasswordParameterDefinition_DisplayName();
        }
        @Override
        public String getHelpFile() {
            return "/help/parameter/string.html";
        }
    }
}
