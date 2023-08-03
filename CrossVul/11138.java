
package hudson.model;
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import hudson.EnvVars;
import hudson.Functions;
import antlr.ANTLRException;
import hudson.AbortException;
import hudson.CopyOnWrite;
import hudson.FeedAdapter;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.cli.declarative.CLIMethod;
import hudson.cli.declarative.CLIResolver;
import hudson.model.Cause.LegacyCodeCause;
import hudson.model.Cause.RemoteCause;
import hudson.model.Cause.UserIdCause;
import hudson.model.Descriptor.FormException;
import hudson.model.Fingerprint.RangeSet;
import hudson.model.Queue.Executable;
import hudson.model.Queue.Task;
import hudson.model.queue.QueueTaskFuture;
import hudson.model.queue.SubTask;
import hudson.model.Queue.WaitingItem;
import hudson.model.RunMap.Constructor;
import hudson.model.labels.LabelAtom;
import hudson.model.labels.LabelExpression;
import hudson.model.listeners.SCMPollListener;
import hudson.model.queue.CauseOfBlockage;
import hudson.model.queue.SubTaskContributor;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import hudson.scm.NullSCM;
import hudson.scm.PollingResult;
import hudson.scm.SCM;
import hudson.scm.SCMRevisionState;
import hudson.scm.SCMS;
import hudson.search.SearchIndexBuilder;
import hudson.security.ACL;
import hudson.security.Permission;
import hudson.slaves.WorkspaceList;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildTrigger;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.tasks.Publisher;
import hudson.triggers.SCMTrigger;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import hudson.util.AlternativeUiTextProvider;
import hudson.util.AlternativeUiTextProvider.Message;
import hudson.util.DescribableList;
import hudson.util.EditDistance;
import hudson.util.FormValidation;
import hudson.widgets.BuildHistoryWidget;
import hudson.widgets.HistoryWidget;
import jenkins.model.Jenkins;
import jenkins.model.JenkinsLocationConfiguration;
import jenkins.model.lazy.AbstractLazyLoadRunMap.Direction;
import jenkins.scm.DefaultSCMCheckoutStrategyImpl;
import jenkins.scm.SCMCheckoutStrategy;
import jenkins.scm.SCMCheckoutStrategyDescriptor;
import jenkins.util.TimeDuration;
import net.sf.json.JSONObject;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.stapler.ForwardToView;
import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.interceptor.RequirePOST;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import static hudson.scm.PollingResult.*;
import static javax.servlet.http.HttpServletResponse.*;
@SuppressWarnings("rawtypes")
public abstract class AbstractProject<P extends AbstractProject<P,R>,R extends AbstractBuild<P,R>> extends Job<P,R> implements BuildableItem {
    private volatile SCM scm = new NullSCM();
    private volatile SCMCheckoutStrategy scmCheckoutStrategy;
    private volatile transient SCMRevisionState pollingBaseline = null;
    @Restricted(NoExternalUse.class)
    @SuppressWarnings("deprecation") 
    protected transient RunMap<R> builds = new RunMap<R>();
    private volatile Integer quietPeriod = null;
    private volatile Integer scmCheckoutRetryCount = null;
    private String assignedNode;
    private volatile boolean canRoam;
    protected volatile boolean disabled;
    protected volatile boolean blockBuildWhenDownstreamBuilding = false;
    protected volatile boolean blockBuildWhenUpstreamBuilding = false;
    private volatile String jdk;
    private volatile BuildAuthorizationToken authToken = null;
    protected List<Trigger<?>> triggers = new Vector<Trigger<?>>();
    @CopyOnWrite
    protected transient volatile List<Action> transientActions = new Vector<Action>();
    private boolean concurrentBuild;
    private String customWorkspace;
    protected AbstractProject(ItemGroup parent, String name) {
        super(parent,name);
        if(!Jenkins.getInstance().getNodes().isEmpty()) {
            canRoam = true;
        }
    }
    @Override
    public synchronized void save() throws IOException {
        super.save();
        updateTransientActions();
    }
    @Override
    public void onCreatedFromScratch() {
        super.onCreatedFromScratch();
        builds = createBuildRunMap();
        updateTransientActions();
    }
    @Override
    public void onLoad(ItemGroup<? extends Item> parent, String name) throws IOException {
        super.onLoad(parent, name);
        RunMap<R> builds = createBuildRunMap();
        RunMap<R> currentBuilds = this.builds;
        if (currentBuilds==null) {
            Item current = parent.getItem(name);
            if (current!=null && current.getClass()==getClass()) {
                currentBuilds = ((AbstractProject)current).builds;
            }
        }
        if (currentBuilds !=null) {
            for (R r : currentBuilds.getLoadedBuilds().values()) {
                if (r.isBuilding())
                    builds.put(r);
            }
        }
        this.builds = builds;
        for (Trigger t : triggers())
            t.start(this, Items.updatingByXml.get());
        if(scm==null)
            scm = new NullSCM(); 
        if(transientActions==null)
            transientActions = new Vector<Action>();    
        updateTransientActions();
    }
    private RunMap<R> createBuildRunMap() {
        return new RunMap<R>(getBuildDir(), new Constructor<R>() {
            public R create(File dir) throws IOException {
                return loadBuild(dir);
            }
        });
    }
    private synchronized List<Trigger<?>> triggers() {
        if (triggers == null) {
            triggers = new Vector<Trigger<?>>();
        }
        return triggers;
    }
    @Override
    public EnvVars getEnvironment(Node node, TaskListener listener) throws IOException, InterruptedException {
        EnvVars env =  super.getEnvironment(node, listener);
        JDK jdk = getJDK();
        if (jdk != null) {
            if (node != null) { 
                jdk = jdk.forNode(node, listener);
            }
            jdk.buildEnvVars(env);
        }
        return env;
    }
    @Override
    protected void performDelete() throws IOException, InterruptedException {
        makeDisabled(true);
        FilePath ws = getWorkspace();
        if(ws!=null) {
            Node on = getLastBuiltOn();
            getScm().processWorkspaceBeforeDeletion(this, ws, on);
            if(on!=null)
                on.getFileSystemProvisioner().discardWorkspace(this,ws);
        }
        super.performDelete();
    }
    @Exported
    public boolean isConcurrentBuild() {
        return concurrentBuild;
    }
    public void setConcurrentBuild(boolean b) throws IOException {
        concurrentBuild = b;
        save();
    }
    public Label getAssignedLabel() {
        if(canRoam)
            return null;
        if(assignedNode==null)
            return Jenkins.getInstance().getSelfLabel();
        return Jenkins.getInstance().getLabel(assignedNode);
    }
    public Set<Label> getRelevantLabels() {
        return Collections.singleton(getAssignedLabel());
    }
    public String getAssignedLabelString() {
        if (canRoam || assignedNode==null)    return null;
        try {
            LabelExpression.parseExpression(assignedNode);
            return assignedNode;
        } catch (ANTLRException e) {
            return LabelAtom.escape(assignedNode);
        }
    }
    public void setAssignedLabel(Label l) throws IOException {
        if(l==null) {
            canRoam = true;
            assignedNode = null;
        } else {
            canRoam = false;
            if(l== Jenkins.getInstance().getSelfLabel())  assignedNode = null;
            else                                        assignedNode = l.getExpression();
        }
        save();
    }
    public void setAssignedNode(Node l) throws IOException {
        setAssignedLabel(l.getSelfLabel());
    }
    @Override
    public String getPronoun() {
        return AlternativeUiTextProvider.get(PRONOUN, this,Messages.AbstractProject_Pronoun());
    }
    public String getBuildNowText() {
        return AlternativeUiTextProvider.get(BUILD_NOW_TEXT,this,Messages.AbstractProject_BuildNow());
    }
    public AbstractProject<?,?> getRootProject() {
        if (this instanceof TopLevelItem) {
            return this;
        } else {
            ItemGroup p = this.getParent();
            if (p instanceof AbstractProject)
                return ((AbstractProject) p).getRootProject();
            return this;
        }
    }
    public final FilePath getWorkspace() {
        AbstractBuild b = getBuildForDeprecatedMethods();
        return b != null ? b.getWorkspace() : null;
    }
    private AbstractBuild getBuildForDeprecatedMethods() {
        Executor e = Executor.currentExecutor();
        if(e!=null) {
            Executable exe = e.getCurrentExecutable();
            if (exe instanceof AbstractBuild) {
                AbstractBuild b = (AbstractBuild) exe;
                if(b.getProject()==this)
                    return b;
            }
        }
        R lb = getLastBuild();
        if(lb!=null)    return lb;
        return null;
    }
    public final FilePath getSomeWorkspace() {
        R b = getSomeBuildWithWorkspace();
        if (b!=null) return b.getWorkspace();
        for (WorkspaceBrowser browser : Jenkins.getInstance().getExtensionList(WorkspaceBrowser.class)) {
            FilePath f = browser.getWorkspace(this);
            if (f != null) return f;
        }
        return null;
    }
    public final R getSomeBuildWithWorkspace() {
        int cnt=0;
        for (R b = getLastBuild(); cnt<5 && b!=null; b=b.getPreviousBuild()) {
            FilePath ws = b.getWorkspace();
            if (ws!=null)   return b;
        }
        return null;
    }
    public FilePath getModuleRoot() {
        AbstractBuild b = getBuildForDeprecatedMethods();
        return b != null ? b.getModuleRoot() : null;
    }
    public FilePath[] getModuleRoots() {
        AbstractBuild b = getBuildForDeprecatedMethods();
        return b != null ? b.getModuleRoots() : null;
    }
    public int getQuietPeriod() {
        return quietPeriod!=null ? quietPeriod : Jenkins.getInstance().getQuietPeriod();
    }
    public SCMCheckoutStrategy getScmCheckoutStrategy() {
        return scmCheckoutStrategy == null ? new DefaultSCMCheckoutStrategyImpl() : scmCheckoutStrategy;
    }
    public void setScmCheckoutStrategy(SCMCheckoutStrategy scmCheckoutStrategy) throws IOException {
        this.scmCheckoutStrategy = scmCheckoutStrategy;
        save();
    }
    public int getScmCheckoutRetryCount() {
        return scmCheckoutRetryCount !=null ? scmCheckoutRetryCount : Jenkins.getInstance().getScmCheckoutRetryCount();
    }
    public boolean getHasCustomQuietPeriod() {
        return quietPeriod!=null;
    }
    public void setQuietPeriod(Integer seconds) throws IOException {
        this.quietPeriod = seconds;
        save();
    }
    public boolean hasCustomScmCheckoutRetryCount(){
        return scmCheckoutRetryCount != null;
    }
    @Override
    public boolean isBuildable() {
        return !isDisabled() && !isHoldOffBuildUntilSave();
    }
    public boolean isConfigurable() {
        return true;
    }
    public boolean blockBuildWhenDownstreamBuilding() {
        return blockBuildWhenDownstreamBuilding;
    }
    public void setBlockBuildWhenDownstreamBuilding(boolean b) throws IOException {
        blockBuildWhenDownstreamBuilding = b;
        save();
    }
    public boolean blockBuildWhenUpstreamBuilding() {
        return blockBuildWhenUpstreamBuilding;
    }
    public void setBlockBuildWhenUpstreamBuilding(boolean b) throws IOException {
        blockBuildWhenUpstreamBuilding = b;
        save();
    }
    public boolean isDisabled() {
        return disabled;
    }
    public FormValidation doCheckRetryCount(@QueryParameter String value)throws IOException,ServletException{
        if(value == null || value.trim().equals(""))
            return FormValidation.ok();
        if (!value.matches("[0-9]*")) {
            return FormValidation.error("Invalid retry count");
        } 
        return FormValidation.ok();
    }
    public void makeDisabled(boolean b) throws IOException {
        if(disabled==b)     return; 
        this.disabled = b;
        if(b)
            Jenkins.getInstance().getQueue().cancel(this);
        save();
    }
    public boolean supportsMakeDisabled() {
        return this instanceof TopLevelItem;
    }
    public void disable() throws IOException {
        makeDisabled(true);
    }
    public void enable() throws IOException {
        makeDisabled(false);
    }
    @Override
    public BallColor getIconColor() {
        if(isDisabled())
            return BallColor.DISABLED;
        else
            return super.getIconColor();
    }
    protected void updateTransientActions() {
        transientActions = createTransientActions();
    }
    protected List<Action> createTransientActions() {
        Vector<Action> ta = new Vector<Action>();
        for (JobProperty<? super P> p : Util.fixNull(properties))
            ta.addAll(p.getJobActions((P)this));
        for (TransientProjectActionFactory tpaf : TransientProjectActionFactory.all())
            ta.addAll(Util.fixNull(tpaf.createFor(this))); 
        return ta;
    }
    public abstract DescribableList<Publisher,Descriptor<Publisher>> getPublishersList();
    @Override
    public void addProperty(JobProperty<? super P> jobProp) throws IOException {
        super.addProperty(jobProp);
        updateTransientActions();
    }
    public List<ProminentProjectAction> getProminentActions() {
        List<Action> a = getActions();
        List<ProminentProjectAction> pa = new Vector<ProminentProjectAction>();
        for (Action action : a) {
            if(action instanceof ProminentProjectAction)
                pa.add((ProminentProjectAction) action);
        }
        return pa;
    }
    @Override
    public void doConfigSubmit( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException, FormException {
        super.doConfigSubmit(req,rsp);
        updateTransientActions();
        Set<AbstractProject> upstream = Collections.emptySet();
        if(req.getParameter("pseudoUpstreamTrigger")!=null) {
            upstream = new HashSet<AbstractProject>(Items.fromNameList(getParent(),req.getParameter("upstreamProjects"),AbstractProject.class));
        }
        Jenkins.getInstance().rebuildDependencyGraph();
        convertUpstreamBuildTrigger(upstream);
        Jenkins.getInstance().getQueue().scheduleMaintenance();
        Jenkins.getInstance().rebuildDependencyGraph();
    }
     void convertUpstreamBuildTrigger(Set<AbstractProject> upstream) throws IOException {
        SecurityContext saveCtx = ACL.impersonate(ACL.SYSTEM);
        try {
            for (AbstractProject<?,?> p : Jenkins.getInstance().getAllItems(AbstractProject.class)) {
                if (!p.isConfigurable()) continue;
                boolean isUpstream = upstream.contains(p);
                synchronized(p) {
                    DescribableList<Publisher,Descriptor<Publisher>> pl = p.getPublishersList();
                    BuildTrigger trigger = pl.get(BuildTrigger.class);
                    List<AbstractProject> newChildProjects = trigger == null ? new ArrayList<AbstractProject>():trigger.getChildProjects(p);
                    if(isUpstream) {
                        if(!newChildProjects.contains(this))
                            newChildProjects.add(this);
                    } else {
                        newChildProjects.remove(this);
                    }
                    if(newChildProjects.isEmpty()) {
                        pl.remove(BuildTrigger.class);
                    } else {
                        List<BuildTrigger> existingList = pl.getAll(BuildTrigger.class);
                        BuildTrigger existing;
                        switch (existingList.size()) {
                        case 0:
                            existing = null;
                            break;
                        case 1:
                            existing = existingList.get(0);
                            break;
                        default:
                            pl.removeAll(BuildTrigger.class);
                            Set<AbstractProject> combinedChildren = new HashSet<AbstractProject>();
                            for (BuildTrigger bt : existingList)
                                combinedChildren.addAll(bt.getChildProjects(p));
                            existing = new BuildTrigger(new ArrayList<AbstractProject>(combinedChildren),existingList.get(0).getThreshold());
                            pl.add(existing);
                            break;
                        }
                        if(existing!=null && existing.hasSame(p,newChildProjects))
                            continue;   
                        pl.replace(new BuildTrigger(newChildProjects,
                            existing==null? Result.SUCCESS:existing.getThreshold()));
                    }
                }
            }
        } finally {
            SecurityContextHolder.setContext(saveCtx);
        }
    }
    public boolean scheduleBuild() {
    	return scheduleBuild(new LegacyCodeCause());
    }
    public boolean scheduleBuild(int quietPeriod) {
    	return scheduleBuild(quietPeriod, new LegacyCodeCause());
    }
    public boolean scheduleBuild(Cause c) {
        return scheduleBuild(getQuietPeriod(), c);
    }
    public boolean scheduleBuild(int quietPeriod, Cause c) {
        return scheduleBuild(quietPeriod, c, new Action[0]);
    }
    public boolean scheduleBuild(int quietPeriod, Cause c, Action... actions) {
        return scheduleBuild2(quietPeriod,c,actions)!=null;
    }
    @WithBridgeMethods(Future.class)
    public QueueTaskFuture<R> scheduleBuild2(int quietPeriod, Cause c, Action... actions) {
        return scheduleBuild2(quietPeriod,c,Arrays.asList(actions));
    }
    @SuppressWarnings("unchecked")
    @WithBridgeMethods(Future.class)
    public QueueTaskFuture<R> scheduleBuild2(int quietPeriod, Cause c, Collection<? extends Action> actions) {
        if (!isBuildable())
            return null;
        List<Action> queueActions = new ArrayList<Action>(actions);
        if (isParameterized() && Util.filter(queueActions, ParametersAction.class).isEmpty()) {
            queueActions.add(new ParametersAction(getDefaultParametersValues()));
        }
        if (c != null) {
            queueActions.add(new CauseAction(c));
        }
        WaitingItem i = Jenkins.getInstance().getQueue().schedule(this, quietPeriod, queueActions);
        if(i!=null)
            return (QueueTaskFuture)i.getFuture();
        return null;
    }
    private List<ParameterValue> getDefaultParametersValues() {
        ParametersDefinitionProperty paramDefProp = getProperty(ParametersDefinitionProperty.class);
        ArrayList<ParameterValue> defValues = new ArrayList<ParameterValue>();
        if(paramDefProp == null)
            return defValues;
        for(ParameterDefinition paramDefinition : paramDefProp.getParameterDefinitions())
        {
           ParameterValue defaultValue  = paramDefinition.getDefaultParameterValue();
            if(defaultValue != null)
                defValues.add(defaultValue);           
        }
        return defValues;
    }
    @SuppressWarnings("deprecation")
    @WithBridgeMethods(Future.class)
    public QueueTaskFuture<R> scheduleBuild2(int quietPeriod) {
        return scheduleBuild2(quietPeriod, new LegacyCodeCause());
    }
    @WithBridgeMethods(Future.class)
    public QueueTaskFuture<R> scheduleBuild2(int quietPeriod, Cause c) {
        return scheduleBuild2(quietPeriod, c, new Action[0]);
    }
    public boolean schedulePolling() {
        if(isDisabled())    return false;
        SCMTrigger scmt = getTrigger(SCMTrigger.class);
        if(scmt==null)      return false;
        scmt.run();
        return true;
    }
    @Override
    public boolean isInQueue() {
        return Jenkins.getInstance().getQueue().contains(this);
    }
    @Override
    public Queue.Item getQueueItem() {
        return Jenkins.getInstance().getQueue().getItem(this);
    }
    public JDK getJDK() {
        return Jenkins.getInstance().getJDK(jdk);
    }
    public void setJDK(JDK jdk) throws IOException {
        this.jdk = jdk.getName();
        save();
    }
    public BuildAuthorizationToken getAuthToken() {
        return authToken;
    }
    @Override
    public RunMap<R> _getRuns() {
        assert builds.baseDirInitialized() : "neither onCreatedFromScratch nor onLoad called on " + this + " yet";
        return builds;
    }
    @Override
    public void removeRun(R run) {
        this.builds.remove(run);
    }
    @Override
    public R getBuild(String id) {
        return builds.getById(id);
    }
    @Override
    public R getBuildByNumber(int n) {
        return builds.getByNumber(n);
    }
    @Override
    public R getFirstBuild() {
        return builds.oldestBuild();
    }
    @Override
    public R getLastBuild() {
        return builds.newestBuild();
    }
    @Override
    public R getNearestBuild(int n) {
        return builds.search(n, Direction.ASC);
    }
    @Override
    public R getNearestOldBuild(int n) {
        return builds.search(n, Direction.DESC);
    }
    protected abstract Class<R> getBuildClass();
    private transient long lastBuildStartTime;
    protected synchronized R newBuild() throws IOException {
    	long timeSinceLast = System.currentTimeMillis() - lastBuildStartTime;
    	if (timeSinceLast < 1000) {
    		try {
				Thread.sleep(1000 - timeSinceLast);
			} catch (InterruptedException e) {
			}
    	}
    	lastBuildStartTime = System.currentTimeMillis();
        try {
            R lastBuild = getBuildClass().getConstructor(getClass()).newInstance(this);
            builds.put(lastBuild);
            return lastBuild;
        } catch (InstantiationException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw handleInvocationTargetException(e);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }
    private IOException handleInvocationTargetException(InvocationTargetException e) {
        Throwable t = e.getTargetException();
        if(t instanceof Error)  throw (Error)t;
        if(t instanceof RuntimeException)   throw (RuntimeException)t;
        if(t instanceof IOException)    return (IOException)t;
        throw new Error(t);
    }
    protected R loadBuild(File dir) throws IOException {
        try {
            return getBuildClass().getConstructor(getClass(),File.class).newInstance(this,dir);
        } catch (InstantiationException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw handleInvocationTargetException(e);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }
    @Override
    public List<Action> getActions() {
        List<Action> actions = new Vector<Action>(super.getActions());
        actions.addAll(transientActions);
        return Collections.unmodifiableList(actions);
    }
    public Node getLastBuiltOn() {
        AbstractBuild b = getLastBuild();
        if(b==null)
            return null;
        else
            return b.getBuiltOn();
    }
    public Object getSameNodeConstraint() {
        return this; 
    }
    public final Task getOwnerTask() {
        return this;
    }
    public boolean isBuildBlocked() {
        return getCauseOfBlockage()!=null;
    }
    public String getWhyBlocked() {
        CauseOfBlockage cb = getCauseOfBlockage();
        return cb!=null ? cb.getShortDescription() : null;
    }
    public static class BecauseOfBuildInProgress extends CauseOfBlockage {
        private final AbstractBuild<?,?> build;
        public BecauseOfBuildInProgress(AbstractBuild<?, ?> build) {
            this.build = build;
        }
        @Override
        public String getShortDescription() {
            Executor e = build.getExecutor();
            String eta = "";
            if (e != null)
                eta = Messages.AbstractProject_ETA(e.getEstimatedRemainingTime());
            int lbn = build.getNumber();
            return Messages.AbstractProject_BuildInProgress(lbn, eta);
        }
    }
    public static class BecauseOfDownstreamBuildInProgress extends CauseOfBlockage {
        public final AbstractProject<?,?> up;
        public BecauseOfDownstreamBuildInProgress(AbstractProject<?,?> up) {
            this.up = up;
        }
        @Override
        public String getShortDescription() {
            return Messages.AbstractProject_DownstreamBuildInProgress(up.getName());
        }
    }
    public static class BecauseOfUpstreamBuildInProgress extends CauseOfBlockage {
        public final AbstractProject<?,?> up;
        public BecauseOfUpstreamBuildInProgress(AbstractProject<?,?> up) {
            this.up = up;
        }
        @Override
        public String getShortDescription() {
            return Messages.AbstractProject_UpstreamBuildInProgress(up.getName());
        }
    }
    public CauseOfBlockage getCauseOfBlockage() {
        if (isLogUpdated() && !isConcurrentBuild())
            return new BecauseOfBuildInProgress(getLastBuild());
        if (blockBuildWhenDownstreamBuilding()) {
            AbstractProject<?,?> bup = getBuildingDownstream();
            if (bup!=null)
                return new BecauseOfDownstreamBuildInProgress(bup);
        }
        if (blockBuildWhenUpstreamBuilding()) {
            AbstractProject<?,?> bup = getBuildingUpstream();
            if (bup!=null)
                return new BecauseOfUpstreamBuildInProgress(bup);
        }
        return null;
    }
    public AbstractProject getBuildingDownstream() {
        Set<Task> unblockedTasks = Jenkins.getInstance().getQueue().getUnblockedTasks();
        for (AbstractProject tup : getTransitiveDownstreamProjects()) {
			if (tup!=this && (tup.isBuilding() || unblockedTasks.contains(tup)))
                return tup;
        }
        return null;
    }
    public AbstractProject getBuildingUpstream() {
        Set<Task> unblockedTasks = Jenkins.getInstance().getQueue().getUnblockedTasks();
        for (AbstractProject tup : getTransitiveUpstreamProjects()) {
			if (tup!=this && (tup.isBuilding() || unblockedTasks.contains(tup)))
                return tup;
        }
        return null;
    }
    public List<SubTask> getSubTasks() {
        List<SubTask> r = new ArrayList<SubTask>();
        r.add(this);
        for (SubTaskContributor euc : SubTaskContributor.all())
            r.addAll(euc.forProject(this));
        for (JobProperty<? super P> p : properties)
            r.addAll(p.getSubTasks());
        return r;
    }
    public R createExecutable() throws IOException {
        if(isDisabled())    return null;
        return newBuild();
    }
    public void checkAbortPermission() {
        checkPermission(AbstractProject.ABORT);
    }
    public boolean hasAbortPermission() {
        return hasPermission(AbstractProject.ABORT);
    }
    public Resource getWorkspaceResource() {
        return new Resource(getFullDisplayName()+" workspace");
    }
    public ResourceList getResourceList() {
        final Set<ResourceActivity> resourceActivities = getResourceActivities();
        final List<ResourceList> resourceLists = new ArrayList<ResourceList>(1 + resourceActivities.size());
        for (ResourceActivity activity : resourceActivities) {
            if (activity != this && activity != null) {
                resourceLists.add(activity.getResourceList());
            }
        }
        return ResourceList.union(resourceLists);
    }
    protected Set<ResourceActivity> getResourceActivities() {
        return Collections.emptySet();
    }
    public boolean checkout(AbstractBuild build, Launcher launcher, BuildListener listener, File changelogFile) throws IOException, InterruptedException {
        SCM scm = getScm();
        if(scm==null)
            return true;    
        FilePath workspace = build.getWorkspace();
        workspace.mkdirs();
        boolean r = scm.checkout(build, launcher, workspace, listener, changelogFile);
        if (r) {
            calcPollingBaseline(build, launcher, listener);
        }
        return r;
    }
    private void calcPollingBaseline(AbstractBuild build, Launcher launcher, TaskListener listener) throws IOException, InterruptedException {
        SCMRevisionState baseline = build.getAction(SCMRevisionState.class);
        if (baseline==null) {
            try {
                baseline = getScm()._calcRevisionsFromBuild(build, launcher, listener);
            } catch (AbstractMethodError e) {
                baseline = SCMRevisionState.NONE; 
            }
            if (baseline!=null)
                build.addAction(baseline);
        }
        pollingBaseline = baseline;
    }
    public boolean pollSCMChanges( TaskListener listener ) {
        return poll(listener).hasChanges();
    }
    public PollingResult poll( TaskListener listener ) {
        SCM scm = getScm();
        if (scm==null) {
            listener.getLogger().println(Messages.AbstractProject_NoSCM());
            return NO_CHANGES;
        }
        if (!isBuildable()) {
            listener.getLogger().println(Messages.AbstractProject_Disabled());
            return NO_CHANGES;
        }
        R lb = getLastBuild();
        if (lb==null) {
            listener.getLogger().println(Messages.AbstractProject_NoBuilds());
            return isInQueue() ? NO_CHANGES : BUILD_NOW;
        }
        if (pollingBaseline==null) {
            R success = getLastSuccessfulBuild(); 
            for (R r=lb; r!=null; r=r.getPreviousBuild()) {
                SCMRevisionState s = r.getAction(SCMRevisionState.class);
                if (s!=null) {
                    pollingBaseline = s;
                    break;
                }
                if (r==success) break;  
            }
        }
        try {
            SCMPollListener.fireBeforePolling(this, listener);
            PollingResult r = _poll(listener, scm, lb);
            SCMPollListener.firePollingSuccess(this,listener, r);
            return r;
        } catch (AbortException e) {
            listener.getLogger().println(e.getMessage());
            listener.fatalError(Messages.AbstractProject_Aborted());
            LOGGER.log(Level.FINE, "Polling "+this+" aborted",e);
            SCMPollListener.firePollingFailed(this, listener,e);
            return NO_CHANGES;
        } catch (IOException e) {
            e.printStackTrace(listener.fatalError(e.getMessage()));
            SCMPollListener.firePollingFailed(this, listener,e);
            return NO_CHANGES;
        } catch (InterruptedException e) {
            e.printStackTrace(listener.fatalError(Messages.AbstractProject_PollingABorted()));
            SCMPollListener.firePollingFailed(this, listener,e);
            return NO_CHANGES;
        } catch (RuntimeException e) {
            SCMPollListener.firePollingFailed(this, listener,e);
            throw e;
        } catch (Error e) {
            SCMPollListener.firePollingFailed(this, listener,e);
            throw e;
        }
    }
    private PollingResult _poll(TaskListener listener, SCM scm, R lb) throws IOException, InterruptedException {
        if (scm.requiresWorkspaceForPolling()) {
            FilePath ws=lb.getWorkspace();
            WorkspaceOfflineReason workspaceOfflineReason = workspaceOffline( lb );
            if ( workspaceOfflineReason != null ) {
                for (WorkspaceBrowser browser : Jenkins.getInstance().getExtensionList(WorkspaceBrowser.class)) {
                    ws = browser.getWorkspace(this);
                    if (ws != null) {
                        return pollWithWorkspace(listener, scm, lb, ws, browser.getWorkspaceList());
                    }
                }
                Label label = getAssignedLabel();
                if (label != null && label.isSelfLabel()) {
                    listener.getLogger().print(Messages.AbstractProject_NoWorkspace());
                    listener.getLogger().println( " (" + workspaceOfflineReason.name() + ")");
                    return NO_CHANGES;
                }
                listener.getLogger().println( ws==null
                    ? Messages.AbstractProject_WorkspaceOffline()
                    : Messages.AbstractProject_NoWorkspace());
                if (isInQueue()) {
                    listener.getLogger().println(Messages.AbstractProject_AwaitingBuildForWorkspace());
                    return NO_CHANGES;
                } else {
                    listener.getLogger().print(Messages.AbstractProject_NewBuildForWorkspace());
                    listener.getLogger().println( " (" + workspaceOfflineReason.name() + ")");
                    return BUILD_NOW;
                }
            } else {
                WorkspaceList l = lb.getBuiltOn().toComputer().getWorkspaceList();
                return pollWithWorkspace(listener, scm, lb, ws, l);
            }
        } else {
            LOGGER.fine("Polling SCM changes of " + getName());
            if (pollingBaseline==null) 
                calcPollingBaseline(lb,null,listener);
            PollingResult r = scm.poll(this, null, null, listener, pollingBaseline);
            pollingBaseline = r.remote;
            return r;
        }
    }
    private PollingResult pollWithWorkspace(TaskListener listener, SCM scm, R lb, FilePath ws, WorkspaceList l) throws InterruptedException, IOException {
        WorkspaceList.Lease lease = l.acquire(ws, !concurrentBuild);
        Launcher launcher = ws.createLauncher(listener).decorateByEnv(getEnvironment(lb.getBuiltOn(),listener));
        try {
            LOGGER.fine("Polling SCM changes of " + getName());
            if (pollingBaseline==null) 
                calcPollingBaseline(lb,launcher,listener);
            PollingResult r = scm.poll(this, launcher, ws, listener, pollingBaseline);
            pollingBaseline = r.remote;
            return r;
        } finally {
            lease.release();
        }
    }
    enum WorkspaceOfflineReason {
        nonexisting_workspace,
        builton_node_gone,
        builton_node_no_executors
    }
    private WorkspaceOfflineReason workspaceOffline(R build) throws IOException, InterruptedException {
        FilePath ws = build.getWorkspace();
        if (ws==null || !ws.exists()) {
            return WorkspaceOfflineReason.nonexisting_workspace;
        }
        Node builtOn = build.getBuiltOn();
        if (builtOn == null) { 
            return WorkspaceOfflineReason.builton_node_gone;
        }
        if (builtOn.toComputer() == null) { 
            return WorkspaceOfflineReason.builton_node_no_executors;
        }
        return null;
    }
    public boolean hasParticipant(User user) {
        for( R build = getLastBuild(); build!=null; build=build.getPreviousBuild())
            if(build.hasParticipant(user))
                return true;
        return false;
    }
    @Exported
    public SCM getScm() {
        return scm;
    }
    public void setScm(SCM scm) throws IOException {
        this.scm = scm;
        save();
    }
    public void addTrigger(Trigger<?> trigger) throws IOException {
        addToList(trigger,triggers());
    }
    public void removeTrigger(TriggerDescriptor trigger) throws IOException {
        removeFromList(trigger,triggers());
    }
    protected final synchronized <T extends Describable<T>>
    void addToList( T item, List<T> collection ) throws IOException {
        for( int i=0; i<collection.size(); i++ ) {
            if(collection.get(i).getDescriptor()==item.getDescriptor()) {
                collection.set(i,item);
                save();
                return;
            }
        }
        collection.add(item);
        save();
        updateTransientActions();
    }
    protected final synchronized <T extends Describable<T>>
    void removeFromList(Descriptor<T> item, List<T> collection) throws IOException {
        for( int i=0; i< collection.size(); i++ ) {
            if(collection.get(i).getDescriptor()==item) {
                collection.remove(i);
                save();
                updateTransientActions();
                return;
            }
        }
    }
    @SuppressWarnings("unchecked")
    public synchronized Map<TriggerDescriptor,Trigger> getTriggers() {
        return (Map)Descriptor.toMap(triggers());
    }
    public <T extends Trigger> T getTrigger(Class<T> clazz) {
        for (Trigger p : triggers()) {
            if(clazz.isInstance(p))
                return clazz.cast(p);
        }
        return null;
    }
    public abstract boolean isFingerprintConfigured();
    @Exported
    public final List<AbstractProject> getDownstreamProjects() {
        return Jenkins.getInstance().getDependencyGraph().getDownstream(this);
    }
    @Exported
    public final List<AbstractProject> getUpstreamProjects() {
        return Jenkins.getInstance().getDependencyGraph().getUpstream(this);
    }
    public final List<AbstractProject> getBuildTriggerUpstreamProjects() {
        ArrayList<AbstractProject> result = new ArrayList<AbstractProject>();
        for (AbstractProject<?,?> ap : getUpstreamProjects()) {
            BuildTrigger buildTrigger = ap.getPublishersList().get(BuildTrigger.class);
            if (buildTrigger != null)
                if (buildTrigger.getChildProjects(ap).contains(this))
                    result.add(ap);
        }        
        return result;
    }    
    public final Set<AbstractProject> getTransitiveUpstreamProjects() {
        return Jenkins.getInstance().getDependencyGraph().getTransitiveUpstream(this);
    }
    public final Set<AbstractProject> getTransitiveDownstreamProjects() {
        return Jenkins.getInstance().getDependencyGraph().getTransitiveDownstream(this);
    }
    public SortedMap<Integer, RangeSet> getRelationship(AbstractProject that) {
        TreeMap<Integer,RangeSet> r = new TreeMap<Integer,RangeSet>(REVERSE_INTEGER_COMPARATOR);
        checkAndRecord(that, r, this.getBuilds());
        return r;
    }
    private void checkAndRecord(AbstractProject that, TreeMap<Integer, RangeSet> r, Collection<R> builds) {
        for (R build : builds) {
            RangeSet rs = build.getDownstreamRelationship(that);
            if(rs==null || rs.isEmpty())
                continue;
            int n = build.getNumber();
            RangeSet value = r.get(n);
            if(value==null)
                r.put(n,rs);
            else
                value.add(rs);
        }
    }
    protected abstract void buildDependencyGraph(DependencyGraph graph);
    @Override
    protected SearchIndexBuilder makeSearchIndex() {
        SearchIndexBuilder sib = super.makeSearchIndex();
        if(isBuildable() && hasPermission(Jenkins.ADMINISTER))
            sib.add("build","build");
        return sib;
    }
    @Override
    protected HistoryWidget createHistoryWidget() {
        return new BuildHistoryWidget<R>(this,builds,HISTORY_ADAPTER);
    }
    public boolean isParameterized() {
        return getProperty(ParametersDefinitionProperty.class) != null;
    }
    public void doBuild( StaplerRequest req, StaplerResponse rsp, @QueryParameter TimeDuration delay ) throws IOException, ServletException {
        if (delay==null)    delay=new TimeDuration(getQuietPeriod());
        ParametersDefinitionProperty pp = getProperty(ParametersDefinitionProperty.class);
        if (pp != null && !req.getMethod().equals("POST")) {
            req.getView(pp, "index.jelly").forward(req, rsp);
            return;
        }
        BuildAuthorizationToken.checkPermission(this, authToken, req, rsp);
        if (pp != null) {
            pp._doBuild(req,rsp,delay);
            return;
        }
        if (!isBuildable())
            throw HttpResponses.error(SC_INTERNAL_SERVER_ERROR,new IOException(getFullName()+" is not buildable"));
        Jenkins.getInstance().getQueue().schedule(this, (int)delay.getTime(), getBuildCause(req));
        rsp.sendRedirect(".");
    }
     CauseAction getBuildCause(StaplerRequest req) {
        Cause cause;
        if (authToken != null && authToken.getToken() != null && req.getParameter("token") != null) {
            String causeText = req.getParameter("cause");
            cause = new RemoteCause(req.getRemoteAddr(), causeText);
        } else {
            cause = new UserIdCause();
        }
        return new CauseAction(cause);
    }
    public int getDelay(StaplerRequest req) throws ServletException {
        String delay = req.getParameter("delay");
        if (delay==null)    return getQuietPeriod();
        try {
            if(delay.endsWith("sec"))   delay=delay.substring(0,delay.length()-3);
            if(delay.endsWith("secs"))  delay=delay.substring(0,delay.length()-4);
            return Integer.parseInt(delay);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid delay parameter value: "+delay);
        }
    }
    public void doBuildWithParameters(StaplerRequest req, StaplerResponse rsp, @QueryParameter TimeDuration delay) throws IOException, ServletException {
        BuildAuthorizationToken.checkPermission(this, authToken, req, rsp);
        ParametersDefinitionProperty pp = getProperty(ParametersDefinitionProperty.class);
        if (pp != null) {
            pp.buildWithParameters(req,rsp,delay);
        } else {
        	throw new IllegalStateException("This build is not parameterized!");
        }
    }
    public void doPolling( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        BuildAuthorizationToken.checkPermission(this, authToken, req, rsp);
        schedulePolling();
        rsp.sendRedirect(".");
    }
    @RequirePOST
    public void doCancelQueue( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        checkPermission(ABORT);
        Jenkins.getInstance().getQueue().cancel(this);
        rsp.forwardToPreviousPage(req);
    }
    @Override
    @RequirePOST
    public void doDoDelete(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException, InterruptedException {
        delete();
        if (req == null || rsp == null)
            return;
        View view = req.findAncestorObject(View.class);
        if (view == null)
            rsp.sendRedirect2(req.getContextPath() + '/' + getParent().getUrl());
        else 
            rsp.sendRedirect2(req.getContextPath() + '/' + view.getUrl());
    }
    @Override
    protected void submit(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException, FormException {
        super.submit(req,rsp);
        JSONObject json = req.getSubmittedForm();
        makeDisabled(req.getParameter("disable")!=null);
        jdk = req.getParameter("jdk");
        if(req.getParameter("hasCustomQuietPeriod")!=null) {
            quietPeriod = Integer.parseInt(req.getParameter("quiet_period"));
        } else {
            quietPeriod = null;
        }
        if(req.getParameter("hasCustomScmCheckoutRetryCount")!=null) {
            scmCheckoutRetryCount = Integer.parseInt(req.getParameter("scmCheckoutRetryCount"));
        } else {
            scmCheckoutRetryCount = null;
        }
        blockBuildWhenDownstreamBuilding = req.getParameter("blockBuildWhenDownstreamBuilding")!=null;
        blockBuildWhenUpstreamBuilding = req.getParameter("blockBuildWhenUpstreamBuilding")!=null;
        if(req.hasParameter("customWorkspace")) {
            customWorkspace = Util.fixEmptyAndTrim(req.getParameter("customWorkspace.directory"));
        } else {
            customWorkspace = null;
        }
        if (json.has("scmCheckoutStrategy"))
            scmCheckoutStrategy = req.bindJSON(SCMCheckoutStrategy.class,
                json.getJSONObject("scmCheckoutStrategy"));
        else
            scmCheckoutStrategy = null;
        if(req.getParameter("hasSlaveAffinity")!=null) {
            assignedNode = Util.fixEmptyAndTrim(req.getParameter("_.assignedLabelString"));
        } else {
            assignedNode = null;
        }
        canRoam = assignedNode==null;
        concurrentBuild = req.getSubmittedForm().has("concurrentBuild");
        authToken = BuildAuthorizationToken.create(req);
        setScm(SCMS.parseSCM(req,this));
        for (Trigger t : triggers())
            t.stop();
        triggers = buildDescribable(req, Trigger.for_(this));
        for (Trigger t : triggers)
            t.start(this,true);
        for (Publisher _t : Descriptor.newInstancesFromHeteroList(req, json, "publisher", Jenkins.getInstance().getExtensionList(BuildTrigger.DescriptorImpl.class))) {
            BuildTrigger t = (BuildTrigger) _t;
            List<AbstractProject> childProjects;
            SecurityContext orig = ACL.impersonate(ACL.SYSTEM);
            try {
                childProjects = t.getChildProjects(this);
            } finally {
                SecurityContextHolder.setContext(orig);
            }
            for (AbstractProject downstream : childProjects) {
                downstream.checkPermission(BUILD);
            }
        }
    }
    protected final <T extends Describable<T>> List<T> buildDescribable(StaplerRequest req, List<? extends Descriptor<T>> descriptors, String prefix) throws FormException, ServletException {
        return buildDescribable(req,descriptors);
    }
    protected final <T extends Describable<T>> List<T> buildDescribable(StaplerRequest req, List<? extends Descriptor<T>> descriptors)
        throws FormException, ServletException {
        JSONObject data = req.getSubmittedForm();
        List<T> r = new Vector<T>();
        for (Descriptor<T> d : descriptors) {
            String safeName = d.getJsonSafeClassName();
            if (req.getParameter(safeName) != null) {
                T instance = d.newInstance(req, data.getJSONObject(safeName));
                r.add(instance);
            }
        }
        return r;
    }
    public DirectoryBrowserSupport doWs( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException, InterruptedException {
        checkPermission(AbstractProject.WORKSPACE);
        FilePath ws = getSomeWorkspace();
        if ((ws == null) || (!ws.exists())) {
            req.getView(this,"noWorkspace.jelly").forward(req,rsp);
            return null;
        } else {
            return new DirectoryBrowserSupport(this, ws, getDisplayName()+" workspace", "folder.png", true);
        }
    }
    public HttpResponse doDoWipeOutWorkspace() throws IOException, ServletException, InterruptedException {
        checkPermission(Functions.isWipeOutPermissionEnabled() ? WIPEOUT : BUILD);
        R b = getSomeBuildWithWorkspace();
        FilePath ws = b!=null ? b.getWorkspace() : null;
        if (ws!=null && getScm().processWorkspaceBeforeDeletion(this, ws, b.getBuiltOn())) {
            ws.deleteRecursive();
            for (WorkspaceListener wl : WorkspaceListener.all()) {
                wl.afterDelete(this);
            }
            return new HttpRedirect(".");
        } else {
            return new ForwardToView(this,"wipeOutWorkspaceBlocked.jelly");
        }
    }
    @CLIMethod(name="disable-job")
    @RequirePOST
    public HttpResponse doDisable() throws IOException, ServletException {
        checkPermission(CONFIGURE);
        makeDisabled(true);
        return new HttpRedirect(".");
    }
    @CLIMethod(name="enable-job")
    @RequirePOST
    public HttpResponse doEnable() throws IOException, ServletException {
        checkPermission(CONFIGURE);
        makeDisabled(false);
        return new HttpRedirect(".");
    }
    public void doRssChangelog(  StaplerRequest req, StaplerResponse rsp  ) throws IOException, ServletException {
        class FeedItem {
            ChangeLogSet.Entry e;
            int idx;
            public FeedItem(Entry e, int idx) {
                this.e = e;
                this.idx = idx;
            }
            AbstractBuild<?,?> getBuild() {
                return e.getParent().build;
            }
        }
        List<FeedItem> entries = new ArrayList<FeedItem>();
        for(R r=getLastBuild(); r!=null; r=r.getPreviousBuild()) {
            int idx=0;
            for( ChangeLogSet.Entry e : r.getChangeSet())
                entries.add(new FeedItem(e,idx++));
        }
        RSS.forwardToRss(
            getDisplayName()+' '+getScm().getDescriptor().getDisplayName()+" changes",
            getUrl()+"changes",
            entries, new FeedAdapter<FeedItem>() {
                public String getEntryTitle(FeedItem item) {
                    return "#"+item.getBuild().number+' '+item.e.getMsg()+" ("+item.e.getAuthor()+")";
                }
                public String getEntryUrl(FeedItem item) {
                    return item.getBuild().getUrl()+"changes#detail"+item.idx;
                }
                public String getEntryID(FeedItem item) {
                    return getEntryUrl(item);
                }
                public String getEntryDescription(FeedItem item) {
                    StringBuilder buf = new StringBuilder();
                    for(String path : item.e.getAffectedPaths())
                        buf.append(path).append('\n');
                    return buf.toString();
                }
                public Calendar getEntryTimestamp(FeedItem item) {
                    return item.getBuild().getTimestamp();
                }
                public String getEntryAuthor(FeedItem entry) {
                    return JenkinsLocationConfiguration.get().getAdminAddress();
                }
            },
            req, rsp );
    }
    public static abstract class AbstractProjectDescriptor extends TopLevelItemDescriptor {
        @Override
        public boolean isApplicable(Descriptor descriptor) {
            return true;
        }
        public FormValidation doCheckAssignedLabelString(@QueryParameter String value) {
            if (Util.fixEmpty(value)==null)
                return FormValidation.ok(); 
            try {
                Label.parseExpression(value);
            } catch (ANTLRException e) {
                return FormValidation.error(e,
                        Messages.AbstractProject_AssignedLabelString_InvalidBooleanExpression(e.getMessage()));
            }
            Label l = Jenkins.getInstance().getLabel(value);
            if (l.isEmpty()) {
                for (LabelAtom a : l.listAtoms()) {
                    if (a.isEmpty()) {
                        LabelAtom nearest = LabelAtom.findNearest(a.getName());
                        return FormValidation.warning(Messages.AbstractProject_AssignedLabelString_NoMatch_DidYouMean(a.getName(),nearest.getDisplayName()));
                    }
                }
                return FormValidation.warning(Messages.AbstractProject_AssignedLabelString_NoMatch());
            }
            return FormValidation.ok();
        }
        public FormValidation doCheckCustomWorkspace(@QueryParameter(value="customWorkspace.directory") String customWorkspace){
        	if(Util.fixEmptyAndTrim(customWorkspace)==null)
        		return FormValidation.error(Messages.AbstractProject_CustomWorkspaceEmpty());
        	else
        		return FormValidation.ok();
        }
        public AutoCompletionCandidates doAutoCompleteUpstreamProjects(@QueryParameter String value) {
            AutoCompletionCandidates candidates = new AutoCompletionCandidates();
            List<Job> jobs = Jenkins.getInstance().getItems(Job.class);
            for (Job job: jobs) {
                if (job.getFullName().startsWith(value)) {
                    if (job.hasPermission(Item.READ)) {
                        candidates.add(job.getFullName());
                    }
                }
            }
            return candidates;
        }
        public AutoCompletionCandidates doAutoCompleteAssignedLabelString(@QueryParameter String value) {
            AutoCompletionCandidates c = new AutoCompletionCandidates();
            Set<Label> labels = Jenkins.getInstance().getLabels();
            List<String> queries = new AutoCompleteSeeder(value).getSeeds();
            for (String term : queries) {
                for (Label l : labels) {
                    if (l.getName().startsWith(term)) {
                        c.add(l.getName());
                    }
                }
            }
            return c;
        }
        public List<SCMCheckoutStrategyDescriptor> getApplicableSCMCheckoutStrategyDescriptors(AbstractProject p) {
            return SCMCheckoutStrategyDescriptor._for(p);
        }
        static class AutoCompleteSeeder {
            private String source;
            AutoCompleteSeeder(String source) {
                this.source = source;
            }
            List<String> getSeeds() {
                ArrayList<String> terms = new ArrayList<String>();
                boolean trailingQuote = source.endsWith("\"");
                boolean leadingQuote = source.startsWith("\"");
                boolean trailingSpace = source.endsWith(" ");
                if (trailingQuote || (trailingSpace && !leadingQuote)) {
                    terms.add("");
                } else {
                    if (leadingQuote) {
                        int quote = source.lastIndexOf('"');
                        if (quote == 0) {
                            terms.add(source.substring(1));
                        } else {
                            terms.add("");
                        }
                    } else {
                        int space = source.lastIndexOf(' ');
                        if (space > -1) {
                            terms.add(source.substring(space+1));
                        } else {
                            terms.add(source);
                        }
                    }
                }
                return terms;
            }
        }
    }
    public static AbstractProject findNearest(String name) {
        return findNearest(name,Hudson.getInstance());
    }
    public static AbstractProject findNearest(String name, ItemGroup context) {
        List<AbstractProject> projects = Hudson.getInstance().getAllItems(AbstractProject.class);
        String[] names = new String[projects.size()];
        for( int i=0; i<projects.size(); i++ )
            names[i] = projects.get(i).getRelativeNameFrom(context);
        String nearest = EditDistance.findNearest(name, names);
        return (AbstractProject)Jenkins.getInstance().getItem(nearest,context);
    }
    private static final Comparator<Integer> REVERSE_INTEGER_COMPARATOR = new Comparator<Integer>() {
        public int compare(Integer o1, Integer o2) {
            return o2-o1;
        }
    };
    private static final Logger LOGGER = Logger.getLogger(AbstractProject.class.getName());
    public static final Permission ABORT = CANCEL;
    public static final Message<AbstractProject> BUILD_NOW_TEXT = new Message<AbstractProject>();
    @CLIResolver
    public static AbstractProject resolveForCLI(
            @Argument(required=true,metaVar="NAME",usage="Job name") String name) throws CmdLineException {
        AbstractProject item = Jenkins.getInstance().getItemByFullName(name, AbstractProject.class);
        if (item==null)
            throw new CmdLineException(null,Messages.AbstractItem_NoSuchJobExists(name,AbstractProject.findNearest(name).getFullName()));
        return item;
    }
    public String getCustomWorkspace() {
        return customWorkspace;
    }
    public void setCustomWorkspace(String customWorkspace) throws IOException {
        this.customWorkspace= Util.fixEmptyAndTrim(customWorkspace);
        save();
    }
}
