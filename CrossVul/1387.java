
package hudson.model;
import hudson.EnvVars;
import hudson.Util;
import hudson.model.queue.SubTask;
import hudson.scm.SCM;
import hudson.tasks.BuildWrapper;
import hudson.tasks.Builder;
import hudson.util.VariableResolver;
import java.io.Serializable;
import java.util.Map;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
@ExportedBean(defaultVisibility=3)
public abstract class ParameterValue implements Serializable {
    protected final String name;
    private String description;
    protected ParameterValue(String name, String description) {
        this.name = name;
        this.description = description;
    }
    protected ParameterValue(String name) {
        this(name, null);
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Exported
    public final String getName() {
        return name;
    }
    @Deprecated
    public void buildEnvVars(AbstractBuild<?,?> build, Map<String,String> env) {
        if (env instanceof EnvVars) {
            if (Util.isOverridden(ParameterValue.class, getClass(), "buildEnvironment", Run.class, EnvVars.class)) {
                buildEnvironment(build, (EnvVars) env);
            } else if (Util.isOverridden(ParameterValue.class, getClass(), "buildEnvVars", AbstractBuild.class, EnvVars.class)) {
                buildEnvVars(build, (EnvVars) env);
            }
        }
    }
    @Deprecated
    public void buildEnvVars(AbstractBuild<?,?> build, EnvVars env) {
        if (Util.isOverridden(ParameterValue.class, getClass(), "buildEnvironment", Run.class, EnvVars.class)) {
            buildEnvironment(build, env);
        } else {
            buildEnvVars(build,(Map<String,String>)env);
        }
    }
    public void buildEnvironment(Run<?,?> build, EnvVars env) {
        if (build instanceof AbstractBuild) {
            buildEnvVars((AbstractBuild) build, env);
        }
    }
    public BuildWrapper createBuildWrapper(AbstractBuild<?,?> build) {
        return null;
    }
    public VariableResolver<String> createVariableResolver(AbstractBuild<?,?> build) {
        return VariableResolver.NONE;
    }
    @Deprecated
    public ParameterDefinition getDefinition() {
        throw new UnsupportedOperationException();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParameterValue other = (ParameterValue) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    public String getShortDescription() {
        return toString();
    }
    public boolean isSensitive() {
        return false;
    }
    public Object getValue() {
        return null;
    }
    public Label getAssignedLabel(SubTask task) {
        return null;
    }
}
