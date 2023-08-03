
package hudson.tasks;
import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.console.ModelHyperlinkNote;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.AutoCompletionCandidates;
import hudson.model.BuildListener;
import hudson.model.Cause.UpstreamCause;
import jenkins.model.DependencyDeclarer;
import hudson.model.DependencyGraph;
import hudson.model.DependencyGraph.Dependency;
import jenkins.model.Jenkins;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Items;
import hudson.model.Job;
import hudson.model.Project;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.ItemListener;
import hudson.tasks.BuildTrigger.DescriptorImpl.ItemListenerImpl;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;
public class BuildTrigger extends Recorder implements DependencyDeclarer {
    private String childProjects;
    private final Result threshold;
    public BuildTrigger(String childProjects, boolean evenIfUnstable) {
        this(childProjects,evenIfUnstable ? Result.UNSTABLE : Result.SUCCESS);
    }
    @DataBoundConstructor
    public BuildTrigger(String childProjects, String threshold) {
        this(childProjects, Result.fromString(StringUtils.defaultString(threshold, Result.SUCCESS.toString())));
    }
    public BuildTrigger(String childProjects, Result threshold) {
        if(childProjects==null)
            throw new IllegalArgumentException();
        this.childProjects = childProjects;
        this.threshold = threshold;
    }
    public BuildTrigger(List<AbstractProject> childProjects, Result threshold) {
        this((Collection<AbstractProject>)childProjects,threshold);
    }
    public BuildTrigger(Collection<? extends AbstractProject> childProjects, Result threshold) {
        this(Items.toNameList(childProjects),threshold);
    }
    public String getChildProjectsValue() {
        return childProjects;
    }
    public Result getThreshold() {
        if(threshold==null)
            return Result.SUCCESS;
        else
            return threshold;
    }
    public List<AbstractProject> getChildProjects() {
        return getChildProjects(Jenkins.getInstance());
    }
    public List<AbstractProject> getChildProjects(AbstractProject owner) {
        return getChildProjects(owner==null?null:owner.getParent());
    }
    public List<AbstractProject> getChildProjects(ItemGroup base) {
        return Items.fromNameList(base,childProjects,AbstractProject.class);
    }
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
    public boolean hasSame(AbstractProject owner, Collection<? extends AbstractProject> projects) {
        List<AbstractProject> children = getChildProjects(owner);
        return children.size()==projects.size() && children.containsAll(projects);
    }
    public boolean hasSame(Collection<? extends AbstractProject> projects) {
        return hasSame(null,projects);
    }
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        return true;
    }
    @Deprecated
    public static boolean execute(AbstractBuild build, BuildListener listener, BuildTrigger trigger) {
        return execute(build, listener);
    }
    public static boolean execute(AbstractBuild build, BuildListener listener) {
        PrintStream logger = listener.getLogger();
        final DependencyGraph graph = Jenkins.getInstance().getDependencyGraph();
        List<Dependency> downstreamProjects = new ArrayList<Dependency>(
                graph.getDownstreamDependencies(build.getProject()));
        Collections.sort(downstreamProjects, new Comparator<Dependency>() {
            public int compare(Dependency lhs, Dependency rhs) {
                return graph.compare(rhs.getDownstreamProject(), lhs.getDownstreamProject());
            }
        });
        for (Dependency dep : downstreamProjects) {
            AbstractProject p = dep.getDownstreamProject();
            if (p.isDisabled()) {
                logger.println(Messages.BuildTrigger_Disabled(ModelHyperlinkNote.encodeTo(p)));
                continue;
            }
            List<Action> buildActions = new ArrayList<Action>();
            if (dep.shouldTriggerBuild(build, listener, buildActions)) {
                String name = ModelHyperlinkNote.encodeTo(p)+" #"+p.getNextBuildNumber();
                if(p.scheduleBuild(p.getQuietPeriod(), new UpstreamCause((Run)build),
                                   buildActions.toArray(new Action[buildActions.size()]))) {
                    logger.println(Messages.BuildTrigger_Triggering(name));
                } else {
                    logger.println(Messages.BuildTrigger_InQueue(name));
                }
            }
        }
        return true;
    }
    public void buildDependencyGraph(AbstractProject owner, DependencyGraph graph) {
        for (AbstractProject p : getChildProjects(owner))
            graph.addDependency(new Dependency(owner, p) {
                @Override
                public boolean shouldTriggerBuild(AbstractBuild build, TaskListener listener,
                                                  List<Action> actions) {
                    return build.getResult().isBetterOrEqualTo(threshold);
                }
            });
    }
    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }
    public boolean onJobRenamed(String oldName, String newName) {
        if(!childProjects.contains(oldName))
            return false;
        boolean changed = false;
        String[] projects = childProjects.split(",");
        for( int i=0; i<projects.length; i++ ) {
            if(projects[i].trim().equals(oldName)) {
                projects[i] = newName;
                changed = true;
            }
        }
        if(changed) {
            StringBuilder b = new StringBuilder();
            for (String p : projects) {
                if(b.length()>0)    b.append(',');
                b.append(p);
            }
            childProjects = b.toString();
        }
        return changed;
    }
    private Object readResolve() {
        if(childProjects==null)
            return childProjects="";
        return this;
    }
    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        public String getDisplayName() {
            return Messages.BuildTrigger_DisplayName();
        }
        @Override
        public String getHelpFile() {
            return "/help/project-config/downstream.html";
        }
        @Override
        public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            String childProjectsString = formData.getString("childProjects").trim();
            if (childProjectsString.endsWith(",")) {
                childProjectsString = childProjectsString.substring(0, childProjectsString.length() - 1).trim();
            }
            return new BuildTrigger(
                childProjectsString,
                formData.optString("threshold", Result.SUCCESS.toString()));
        }
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
        public boolean showEvenIfUnstableOption(@CheckForNull Class<? extends AbstractProject<?,?>> jobType) {
            return jobType == null || !jobType.getName().contains("PromotionProcess");
        }
        public FormValidation doCheck(@AncestorInPath Item project, @QueryParameter String value, @QueryParameter boolean upstream) {
            if(!project.hasPermission(Item.CONFIGURE))      return FormValidation.ok();
            StringTokenizer tokens = new StringTokenizer(Util.fixNull(value),",");
            boolean hasProjects = false;
            while(tokens.hasMoreTokens()) {
                String projectName = tokens.nextToken().trim();
                if (StringUtils.isNotBlank(projectName)) {
                    Item item = Jenkins.getInstance().getItem(projectName,project,Item.class);
                    if(item==null)
                        return FormValidation.error(Messages.BuildTrigger_NoSuchProject(projectName,
                                AbstractProject.findNearest(projectName,project.getParent()).getRelativeNameFrom(project)));
                    if(!(item instanceof AbstractProject))
                        return FormValidation.error(Messages.BuildTrigger_NotBuildable(projectName));
                    if (!upstream && !item.hasPermission(Item.BUILD)) {
                        return FormValidation.error(Messages.BuildTrigger_you_have_no_permission_to_build_(projectName));
                    }
                    hasProjects = true;
                }
            }
            if (!hasProjects) {
                return FormValidation.error(Messages.BuildTrigger_NoProjectSpecified());
            }
            return FormValidation.ok();
        }
        public AutoCompletionCandidates doAutoCompleteChildProjects(@QueryParameter String value, @AncestorInPath Item self, @AncestorInPath ItemGroup container) {
            return AutoCompletionCandidates.ofJobNames(Job.class,value,self,container);
        }
        @Extension
        public static class ItemListenerImpl extends ItemListener {
            @Override
            public void onRenamed(Item item, String oldName, String newName) {
                for( Project<?,?> p : Jenkins.getInstance().getAllItems(Project.class) ) {
                    BuildTrigger t = p.getPublishersList().get(BuildTrigger.class);
                    if(t!=null) {
                        if(t.onJobRenamed(oldName,newName)) {
                            try {
                                p.save();
                            } catch (IOException e) {
                                LOGGER.log(Level.WARNING, "Failed to persist project setting during rename from "+oldName+" to "+newName,e);
                            }
                        }
                    }
                }
            }
        }
    }
    private static final Logger LOGGER = Logger.getLogger(BuildTrigger.class.getName());
}
