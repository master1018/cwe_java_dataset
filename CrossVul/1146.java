
package hudson.scm;
import static hudson.Util.fixEmptyAndTrim;
import static hudson.scm.PollingResult.BUILD_NOW;
import static hudson.scm.PollingResult.NO_CHANGES;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.Functions;
import hudson.Launcher;
import hudson.Util;
import hudson.XmlFile;
import hudson.model.BuildListener;
import hudson.model.Item;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Computer;
import hudson.model.Hudson;
import java.util.Arrays;
import java.util.WeakHashMap;
import jenkins.model.Jenkins.MasterComputer;
import hudson.model.Node;
import hudson.model.ParametersAction;
import hudson.model.Run;
import hudson.remoting.Callable;
import hudson.remoting.Channel;
import hudson.remoting.VirtualChannel;
import hudson.scm.UserProvidedCredential.AuthenticationManagerImpl;
import hudson.scm.subversion.CheckoutUpdater;
import hudson.scm.subversion.Messages;
import hudson.scm.subversion.SvnHelper;
import hudson.scm.subversion.UpdateUpdater;
import hudson.scm.subversion.UpdateWithRevertUpdater;
import hudson.scm.subversion.UpdaterException;
import hudson.scm.subversion.WorkspaceUpdater;
import hudson.scm.subversion.WorkspaceUpdater.UpdateTask;
import hudson.scm.subversion.WorkspaceUpdaterDescriptor;
import hudson.util.EditDistance;
import hudson.util.FormValidation;
import hudson.util.LogTaskListener;
import hudson.util.MultipartFormDataParser;
import hudson.util.Scrambler;
import hudson.util.Secret;
import hudson.util.TimeUnit2;
import hudson.util.XStream2;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.ServletException;
import javax.xml.transform.stream.StreamResult;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Chmod;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationOutcomeListener;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationProvider;
import org.tmatesoft.svn.core.auth.SVNAuthentication;
import org.tmatesoft.svn.core.auth.SVNPasswordAuthentication;
import org.tmatesoft.svn.core.auth.SVNSSHAuthentication;
import org.tmatesoft.svn.core.auth.SVNSSLAuthentication;
import org.tmatesoft.svn.core.auth.SVNUserNameAuthentication;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.dav.http.DefaultHTTPConnectionFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.util.SVNPathUtil;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.internal.wc.admin.SVNAdminAreaFactory;
import org.tmatesoft.svn.core.io.SVNCapability;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import com.thoughtworks.xstream.XStream;
import com.trilead.ssh2.DebugLogger;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.crypto.Base64;
@SuppressWarnings("rawtypes")
public class SubversionSCM extends SCM implements Serializable {
    private ModuleLocation[] locations = new ModuleLocation[0];
    private final SubversionRepositoryBrowser browser;
    private String excludedRegions;
    private String includedRegions;
    private String excludedUsers;
    private String excludedRevprop;
    private String excludedCommitMessages;
    private WorkspaceUpdater workspaceUpdater;
    @Deprecated
    private String modules;
    @Deprecated
    private Boolean useUpdate;
    @Deprecated
    private Boolean doRevert;
    private boolean ignoreDirPropChanges;
    private boolean filterChangelog;
    private transient Map<AbstractProject, List<External>> projectExternalsCache;
    private transient boolean pollFromMaster = POLL_FROM_MASTER;
    public SubversionSCM(String[] remoteLocations, String[] localLocations,
                         boolean useUpdate, SubversionRepositoryBrowser browser) {
        this(remoteLocations,localLocations, useUpdate, browser, null, null, null);
    }
    public SubversionSCM(String[] remoteLocations, String[] localLocations,
                         boolean useUpdate, SubversionRepositoryBrowser browser, String excludedRegions) {
        this(ModuleLocation.parse(remoteLocations,localLocations,null,null), useUpdate, false, browser, excludedRegions, null, null, null);
    }
     public SubversionSCM(String[] remoteLocations, String[] localLocations,
                         boolean useUpdate, SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop) {
        this(ModuleLocation.parse(remoteLocations,localLocations,null,null), useUpdate, false, browser, excludedRegions, excludedUsers, excludedRevprop, null);
    }
    public SubversionSCM(List<ModuleLocation> locations,
                         boolean useUpdate, SubversionRepositoryBrowser browser, String excludedRegions) {
        this(locations, useUpdate, false, browser, excludedRegions, null, null, null);
    }
    public SubversionSCM(List<ModuleLocation> locations,
            boolean useUpdate, SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop) {
        this(locations, useUpdate, false, browser, excludedRegions, excludedUsers, excludedRevprop, null);
    }
    public SubversionSCM(List<ModuleLocation> locations,
            boolean useUpdate, SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop, String excludedCommitMessages) {
    	this(locations, useUpdate, false, browser, excludedRegions, excludedUsers, excludedRevprop, excludedCommitMessages);
    }
    public SubversionSCM(List<ModuleLocation> locations,
                         boolean useUpdate, boolean doRevert, SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop, String excludedCommitMessages) {
        this(locations, useUpdate, doRevert, browser, excludedRegions, excludedUsers, excludedRevprop, excludedCommitMessages, null);
    }
    public SubversionSCM(List<ModuleLocation> locations,
                         boolean useUpdate, boolean doRevert, SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop, String excludedCommitMessages,
                         String includedRegions) {
        this(locations, useUpdate?(doRevert?new UpdateWithRevertUpdater():new UpdateUpdater()):new CheckoutUpdater(),
                browser, excludedRegions, excludedUsers, excludedRevprop, excludedCommitMessages, includedRegions);
    }
    public SubversionSCM(List<ModuleLocation> locations, WorkspaceUpdater workspaceUpdater,
                         SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop, String excludedCommitMessages,
                         String includedRegions) {
      this(locations, workspaceUpdater, browser, excludedRegions, excludedUsers, excludedRevprop, excludedCommitMessages, includedRegions, false);
    }
    public SubversionSCM(List<ModuleLocation> locations, WorkspaceUpdater workspaceUpdater,
            SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop, String excludedCommitMessages,
            String includedRegions, boolean ignoreDirPropChanges) {
        this(locations, workspaceUpdater, browser, excludedRegions, excludedUsers, excludedRevprop, excludedCommitMessages, includedRegions, ignoreDirPropChanges, false);
    }
    @DataBoundConstructor
    public SubversionSCM(List<ModuleLocation> locations, WorkspaceUpdater workspaceUpdater,
                         SubversionRepositoryBrowser browser, String excludedRegions, String excludedUsers, String excludedRevprop, String excludedCommitMessages,
                         String includedRegions, boolean ignoreDirPropChanges, boolean filterChangelog) {
        for (Iterator<ModuleLocation> itr = locations.iterator(); itr.hasNext();) {
            ModuleLocation ml = itr.next();
            String remote = Util.fixEmptyAndTrim(ml.remote);
            if(remote==null) itr.remove();
        }
        this.locations = locations.toArray(new ModuleLocation[locations.size()]);
        this.workspaceUpdater = workspaceUpdater;
        this.browser = browser;
        this.excludedRegions = excludedRegions;
        this.excludedUsers = excludedUsers;
        this.excludedRevprop = excludedRevprop;
        this.excludedCommitMessages = excludedCommitMessages;
        this.includedRegions = includedRegions;
        this.ignoreDirPropChanges = ignoreDirPropChanges;
        this.filterChangelog = filterChangelog;
    }
    public SubversionSCM(String svnUrl) {
        this(svnUrl,".");
    }
    public SubversionSCM(String svnUrl, String local) {
        this(new String[]{svnUrl},new String[]{local},true,null,null,null,null);
    }
    public SubversionSCM(String[] svnUrls, String[] locals) {
        this(svnUrls,locals,true,null,null,null,null);
    }
    public String getModules() {
        return null;
    }
    @Exported
    public ModuleLocation[] getLocations() {
    	return getLocations(null, null);
    }
    @Exported
    public WorkspaceUpdater getWorkspaceUpdater() {
        if (workspaceUpdater!=null)
            return workspaceUpdater;
        if (useUpdate!=null && !useUpdate)
            return new CheckoutUpdater();
        if (doRevert!=null && doRevert)
            return new UpdateWithRevertUpdater();
        return new UpdateUpdater();
    }
    public void setWorkspaceUpdater(WorkspaceUpdater workspaceUpdater) {
        this.workspaceUpdater = workspaceUpdater;
    }
    public ModuleLocation[] getLocations(AbstractBuild<?,?> build) {
        return getLocations(null, build);
    }
    public ModuleLocation[] getLocations(EnvVars env, AbstractBuild<?,?> build) {
        if (modules != null) {
            List<ModuleLocation> oldLocations = new ArrayList<ModuleLocation>();
            StringTokenizer tokens = new StringTokenizer(modules);
            while (tokens.hasMoreTokens()) {
                String remoteLoc = Util.removeTrailingSlash(tokens.nextToken());
                oldLocations.add(new ModuleLocation(remoteLoc, null));
            }
            locations = oldLocations.toArray(new ModuleLocation[oldLocations.size()]);
            modules = null;
        }
        if(env == null && build == null)
            return locations;
        ModuleLocation[] outLocations = new ModuleLocation[locations.length];
        EnvVars env2 = env != null ? new EnvVars(env) : new EnvVars();
        if (build != null) {
            env2.putAll(build.getBuildVariables());
        }
        EnvVars.resolve(env2);
        for (int i = 0; i < outLocations.length; i++) {
            outLocations[i] = locations[i].getExpandedLocation(env2);
        }
        return outLocations;
    }
    public ModuleLocation[] getProjectLocations(AbstractProject project) throws IOException {
        List<External> projectExternals = getExternals(project);
        ModuleLocation[] configuredLocations = getLocations();
        if (projectExternals.isEmpty()) {
            return configuredLocations;
        }
        List<ModuleLocation> allLocations = new ArrayList<ModuleLocation>(configuredLocations.length + projectExternals.size());
        allLocations.addAll(Arrays.asList(configuredLocations));
        for (External external : projectExternals) {
            allLocations.add(new ModuleLocation(external.url, external.path));
        }
        return allLocations.toArray(new ModuleLocation[allLocations.size()]);
    }
    private List<External> getExternals(AbstractProject context) throws IOException {
        Map<AbstractProject, List<External>> projectExternalsCache = getProjectExternalsCache();
        List<External> projectExternals;
        synchronized (projectExternalsCache) {
            projectExternals = projectExternalsCache.get(context);
        }
        if (projectExternals == null) {
            projectExternals = parseExternalsFile(context);
            synchronized (projectExternalsCache) {
                if (!projectExternalsCache.containsKey(context)) {
                    projectExternalsCache.put(context, projectExternals);
                }
            }
        }
        return projectExternals;
    }
    @Override
    @Exported
    public SubversionRepositoryBrowser getBrowser() {
        return browser;
    }
    @Exported
    public String getExcludedRegions() {
        return excludedRegions;
    }
    public String[] getExcludedRegionsNormalized() {
        return (excludedRegions == null || excludedRegions.trim().equals(""))
                ? null : excludedRegions.split("[\\r\\n]+");
    }
    private Pattern[] getExcludedRegionsPatterns() {
        String[] excluded = getExcludedRegionsNormalized();
        if (excluded != null) {
            Pattern[] patterns = new Pattern[excluded.length];
            int i = 0;
            for (String excludedRegion : excluded) {
                patterns[i++] = Pattern.compile(excludedRegion);
            }
            return patterns;
        }
        return new Pattern[0];
    }
    @Exported
    public String getIncludedRegions() {
        return includedRegions;
    }
    public String[] getIncludedRegionsNormalized() {
        return (includedRegions == null || includedRegions.trim().equals(""))
                ? null : includedRegions.split("[\\r\\n]+");
    }
    private Pattern[] getIncludedRegionsPatterns() {
        String[] included = getIncludedRegionsNormalized();
        if (included != null) {
            Pattern[] patterns = new Pattern[included.length];
            int i = 0;
            for (String includedRegion : included) {
                patterns[i++] = Pattern.compile(includedRegion);
            }
            return patterns;
        }
        return new Pattern[0];
    }
    @Exported
    public String getExcludedUsers() {
        return excludedUsers;
    }
    public Set<String> getExcludedUsersNormalized() {
        String s = fixEmptyAndTrim(excludedUsers);
        if (s==null)
            return Collections.emptySet();
        Set<String> users = new HashSet<String>();
        for (String user : s.split("[\\r\\n]+"))
            users.add(user.trim());
        return users;
    }
    @Exported
    public String getExcludedRevprop() {
        return excludedRevprop;
    }
    @Exported
    public String getExcludedCommitMessages() {
        return excludedCommitMessages;
    }
    public String[] getExcludedCommitMessagesNormalized() {
        String s = fixEmptyAndTrim(excludedCommitMessages);
        return s == null ? new String[0] : s.split("[\\r\\n]+");
    }
    private Pattern[] getExcludedCommitMessagesPatterns() {
        String[] excluded = getExcludedCommitMessagesNormalized();
        Pattern[] patterns = new Pattern[excluded.length];
        int i = 0;
        for (String excludedCommitMessage : excluded) {
            patterns[i++] = Pattern.compile(excludedCommitMessage);
        }
        return patterns;
    }
    @Exported
    public boolean isIgnoreDirPropChanges() {
      return ignoreDirPropChanges;
    }
    @Exported
    public boolean isFilterChangelog() {
      return filterChangelog;
    }
    @Override
    public void buildEnvVars(AbstractBuild<?, ?> build, Map<String, String> env) {
        super.buildEnvVars(build, env);
        ModuleLocation[] svnLocations = getLocations(new EnvVars(env), build);
        try {
            Map<String,Long> revisions = parseSvnRevisionFile(build);
            Set<String> knownURLs = revisions.keySet();
            if(svnLocations.length==1) {
                String url = svnLocations[0].getURL();
                Long rev = revisions.get(url);
                if(rev!=null) {
                    env.put("SVN_REVISION",rev.toString());
                    env.put("SVN_URL",url);
                } else if (!knownURLs.isEmpty()) {
                    LOGGER.log(WARNING, "no revision found corresponding to {0}; known: {1}", new Object[] {url, knownURLs});
                }
            }
            for(int i=0;i<svnLocations.length;i++) {
                String url = svnLocations[i].getURL();
                Long rev = revisions.get(url);
                if(rev!=null) {
                    env.put("SVN_REVISION_"+(i+1),rev.toString());
                    env.put("SVN_URL_"+(i+1),url);
                } else if (!knownURLs.isEmpty()) {
                    LOGGER.log(WARNING, "no revision found corresponding to {0}; known: {1}", new Object[] {url, knownURLs});
                }
            }
        } catch (IOException e) {
            LOGGER.log(WARNING, "error building environment variables", e);
        }
    }
    private boolean calcChangeLog(AbstractBuild<?,?> build, File changelogFile, BuildListener listener, List<External> externals, EnvVars env) throws IOException, InterruptedException {
        if(build.getPreviousBuild()==null) {
            return createEmptyChangeLog(changelogFile, listener, "log");
        }
        OutputStream os = new BufferedOutputStream(new FileOutputStream(changelogFile));
        boolean created;
        try {
            created = new SubversionChangeLogBuilder(build, env, listener, this).run(externals, new StreamResult(os));
        } finally {
            os.close();
        }
        if(!created)
            createEmptyChangeLog(changelogFile, listener, "log");
        return true;
    }
     static Map<String,Long> parseRevisionFile(AbstractBuild<?,?> build) throws IOException {
        return parseRevisionFile(build,true,false);
    }
     Map<String,Long> parseSvnRevisionFile(AbstractBuild<?,?> build) throws IOException {
        return parseRevisionFile(build);
    }
     static Map<String,Long> parseRevisionFile(AbstractBuild<?,?> build, boolean findClosest, boolean prunePinnedExternals) throws IOException {
        Map<String,Long> revisions = new HashMap<String,Long>(); 
        if (findClosest) {
            for (AbstractBuild<?,?> b=build; b!=null; b=b.getPreviousBuild()) {
                if(getRevisionFile(b).exists()) {
                    build = b;
                    break;
                }
            }
        }
        {
            File file = getRevisionFile(build);
            if(!file.exists())
                return revisions;
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String line;
                while((line=br.readLine())!=null) {
                	boolean isPinned = false;
                	int indexLast = line.length();
                	if (line.lastIndexOf("::p") == indexLast-3) {
                		isPinned = true;
                		indexLast -= 3;
                	}
                	int index = line.lastIndexOf('/');
                    if(index<0) {
                        continue;   
                    }
                    try {
                    	String url = line.substring(0, index);
                    	long revision = Long.parseLong(line.substring(index+1,indexLast));
                    	Long oldRevision = revisions.get(url);
                    	if (isPinned) {
                    		if (!prunePinnedExternals) {
                    			if (oldRevision == null)
                    				revisions.put(url, revision);
                    		}
                    	} else {
                        	if (oldRevision == null || oldRevision > revision)
                        		revisions.put(url, revision);
                    	}
                	} catch (NumberFormatException e) {
                	    LOGGER.log(WARNING, "Error parsing line " + line, e);
                	}
                }
            } finally {
                br.close();
            }
        }
        return revisions;
    }
     @SuppressWarnings("unchecked")
    static List<External> parseExternalsFile(AbstractProject project) throws IOException {
        File file = getExternalsFile(project);
        if(file.exists()) {
            try {
                return (List<External>)new XmlFile(External.XSTREAM,file).read();
            } catch (IOException e) {
            }
        }
        return Collections.emptyList();
    }
    @Override
    public boolean requiresWorkspaceForPolling() {
        return false;
    }
    @SuppressWarnings("unchecked")
    public boolean checkout(AbstractBuild build, Launcher launcher, FilePath workspace, final BuildListener listener, File changelogFile) throws IOException, InterruptedException {
        EnvVars env = build.getEnvironment(listener);
        EnvVarsUtils.overrideAll(env, build.getBuildVariables());
        List<External> externals = null;
        try {
            externals = checkout(build,workspace,listener,env);
        } catch (UpdaterException e) {
            return false;
        }
        PrintWriter w = new PrintWriter(new FileOutputStream(getRevisionFile(build)));
        try {
            List<SvnInfoP> pList = workspace.act(new BuildRevisionMapTask(build, this, listener, externals, env));
            List<SvnInfo> revList= new ArrayList<SvnInfo>(pList.size());
            for (SvnInfoP p: pList) {
                if (p.pinned) 
                    w.println( p.info.url +'/'+ p.info.revision + "::p");
                else
                    w.println( p.info.url +'/'+ p.info.revision);
                revList.add(p.info);
            }
            build.addAction(new SubversionTagAction(build,revList));
        } finally {
            w.close();
        }
        new XmlFile(External.XSTREAM,getExternalsFile(build.getProject())).write(externals);
        Map<AbstractProject, List<External>> projectExternalsCache = getProjectExternalsCache();
        synchronized (projectExternalsCache) {
            projectExternalsCache.put(build.getProject(), externals);
        }
        return calcChangeLog(build, changelogFile, listener, externals, env);
    }
    private List<External> checkout(AbstractBuild build, FilePath workspace, TaskListener listener, EnvVars env) throws IOException, InterruptedException {
        if (repositoryLocationsNoLongerExist(build, listener, env)) {
            Run lsb = build.getProject().getLastSuccessfulBuild();
            if (lsb != null && build.getNumber()-lsb.getNumber()>10
            && build.getTimestamp().getTimeInMillis()-lsb.getTimestamp().getTimeInMillis() > TimeUnit2.DAYS.toMillis(1)) {
                listener.getLogger().println("One or more repository locations do not exist anymore for " + build.getProject().getName() + ", project will be disabled.");
                build.getProject().makeDisabled(true);
                return null;
            }
        }
        List<External> externals = new ArrayList<External>();
        for (ModuleLocation location : getLocations(env, build)) {
            externals.addAll( workspace.act(new CheckOutTask(build, this, location, build.getTimestamp().getTime(), listener, env)));
        }
        return externals;
    }
    private synchronized Map<AbstractProject, List<External>> getProjectExternalsCache() {
        if (projectExternalsCache == null) {
            projectExternalsCache = new WeakHashMap<AbstractProject, List<External>>();
        }
        return projectExternalsCache;
    }
    private static class CheckOutTask extends UpdateTask implements FileCallable<List<External>> {
        private final UpdateTask task;
         public CheckOutTask(AbstractBuild<?, ?> build, SubversionSCM parent, ModuleLocation location, Date timestamp, TaskListener listener, EnvVars env) {
            this.authProvider = parent.getDescriptor().createAuthenticationProvider(build.getParent());
            this.timestamp = timestamp;
            this.listener = listener;
            this.location = location;
            this.revisions = build.getAction(RevisionParameterAction.class);
            this.task = parent.getWorkspaceUpdater().createTask();
        }
        public List<External> invoke(File ws, VirtualChannel channel) throws IOException {
            clientManager = createClientManager(authProvider);
            manager = clientManager.getCore();
            this.ws = ws;
            try {
                List<External> externals = perform();
                checkClockOutOfSync();
                return externals;
            } catch (InterruptedException e) {
                throw (InterruptedIOException)new InterruptedIOException().initCause(e);
            } finally {
                clientManager.dispose();
            }
        }
        @Override
        public List<External> perform() throws IOException, InterruptedException {
            return delegateTo(task);
        }
        private void checkClockOutOfSync() {
            try {
                SVNDirEntry dir = clientManager.createRepository(location.getSVNURL(), true).info("/", -1);
                if (dir != null) {
                    if (dir.getDate() != null && dir.getDate().after(new Date())) 
                    {
                        listener.getLogger().println(Messages.SubversionSCM_ClockOutOfSync());
                    }
                }
            } catch (SVNAuthenticationException e) {
                LOGGER.log(Level.FINE,"Failed to estimate the remote time stamp",e);
            } catch (SVNException e) {
                LOGGER.log(Level.INFO,"Failed to estimate the remote time stamp",e);
            }
        }
        private static final long serialVersionUID = 1L;
    }
    public static SVNClientManager createSvnClientManager(ISVNAuthenticationProvider authProvider) {
        return createClientManager(authProvider).getCore();
    }
    public static SvnClientManager createClientManager(ISVNAuthenticationProvider authProvider) {
        ISVNAuthenticationManager sam = createSvnAuthenticationManager(authProvider);
        return new SvnClientManager(SVNClientManager.newInstance(createDefaultSVNOptions(), sam));
    }
    public static DefaultSVNOptions createDefaultSVNOptions() {
        DefaultSVNOptions defaultOptions = SVNWCUtil.createDefaultOptions(true);
        DescriptorImpl descriptor = Hudson.getInstance() == null ? null : Hudson.getInstance().getDescriptorByType(DescriptorImpl.class);
        if (defaultOptions != null && descriptor != null) {
            defaultOptions.setAuthStorageEnabled(descriptor.isStoreAuthToDisk());
        }
        return defaultOptions;
    }
    public static ISVNAuthenticationManager createSvnAuthenticationManager(ISVNAuthenticationProvider authProvider) {
        File configDir;
        if (CONFIG_DIR!=null)
            configDir = new File(CONFIG_DIR);
        else
            configDir = SVNWCUtil.getDefaultConfigurationDirectory();
        ISVNAuthenticationManager sam = SVNWCUtil.createDefaultAuthenticationManager(configDir, null, null);
        sam.setAuthenticationProvider(authProvider);
        SVNAuthStoreHandlerImpl.install(sam);
        return sam;
    }
    public static SVNClientManager createSvnClientManager(AbstractProject context) {
        return createClientManager(context).getCore();
    }
    public static SvnClientManager createClientManager(AbstractProject context) {
        return new SvnClientManager(createSvnClientManager(Hudson.getInstance().getDescriptorByType(DescriptorImpl.class).createAuthenticationProvider(context)));
    }
    public static final class SvnInfo implements Serializable, Comparable<SvnInfo> {
        public final String url;
        public final long revision;
        public SvnInfo(String url, long revision) {
            this.url = url;
            this.revision = revision;
        }
        public SvnInfo(SVNInfo info) {
            this( info.getURL().toDecodedString(), info.getCommittedRevision().getNumber() );
        }
        public SVNURL getSVNURL() throws SVNException {
            return SVNURL.parseURIDecoded(url);
        }
        public int compareTo(SvnInfo that) {
            int r = this.url.compareTo(that.url);
            if(r!=0)    return r;
            if(this.revision<that.revision) return -1;
            if(this.revision>that.revision) return +1;
            return 0;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SvnInfo svnInfo = (SvnInfo) o;
            return revision==svnInfo.revision && url.equals(svnInfo.url);
        }
        @Override
        public int hashCode() {
            int result;
            result = url.hashCode();
            result = 31 * result + (int) (revision ^ (revision >>> 32));
            return result;
        }
        @Override
        public String toString() {
            return String.format("%s (rev.%s)",url,revision);
        }
        private static final long serialVersionUID = 1L;
    }
    private static final class SvnInfoP implements Serializable {
        public final SvnInfo info;
        public final boolean pinned;
        public SvnInfoP(SvnInfo info, boolean pinned) {
            this.info = info;
            this.pinned = pinned;
        }
        private static final long serialVersionUID = 1L;
    }
    public static final class External implements Serializable {
        public final String path;
        public final String url;
        public final long revision;
        public External(String path, SVNURL url, long revision) {
            this.path = path;
            this.url = url.toDecodedString();
            this.revision = revision;
        }
        public boolean isRevisionFixed() {
            return revision!=-1;
        }
        private static final long serialVersionUID = 1L;
        private static final XStream XSTREAM = new XStream2();
        static {
            XSTREAM.alias("external",External.class);
        }
    }
    static SVNInfo parseSvnInfo(SVNURL remoteUrl, ISVNAuthenticationProvider authProvider) throws SVNException {
        final SvnClientManager manager = createClientManager(authProvider);
        try {
            final SVNWCClient svnWc = manager.getWCClient();
            return svnWc.doInfo(remoteUrl, SVNRevision.HEAD, SVNRevision.HEAD);
        } finally {
            manager.dispose();
        }
    }
    private static class BuildRevisionMapTask implements FileCallable<List<SvnInfoP>> {
        private final ISVNAuthenticationProvider authProvider;
        private final TaskListener listener;
        private final List<External> externals;
        private final ModuleLocation[] locations;
        public BuildRevisionMapTask(AbstractBuild<?, ?> build, SubversionSCM parent, TaskListener listener, List<External> externals, EnvVars env) {
            this.authProvider = parent.getDescriptor().createAuthenticationProvider(build.getParent());
            this.listener = listener;
            this.externals = externals;
            this.locations = parent.getLocations(env, build);
        }
        public List<SvnInfoP> invoke(File ws, VirtualChannel channel) throws IOException {
            List<SvnInfoP> revisions = new ArrayList<SvnInfoP>();
            final SvnClientManager manager = createClientManager(authProvider);
            try {
                final SVNWCClient svnWc = manager.getWCClient();
                for( ModuleLocation module : locations ) {
                    try {
                        SvnInfo info = new SvnInfo(svnWc.doInfo(new File(ws,module.getLocalDir()), SVNRevision.WORKING));
                        revisions.add(new SvnInfoP(info, false));
                    } catch (SVNException e) {
                        e.printStackTrace(listener.error("Failed to parse svn info for "+module.remote));
                    }
                }
                for(External ext : externals){
                    try {
                        SvnInfo info = new SvnInfo(svnWc.doInfo(new File(ws,ext.path),SVNRevision.WORKING));
                        revisions.add(new SvnInfoP(info, ext.isRevisionFixed()));
                    } catch (SVNException e) {
                        e.printStackTrace(listener.error("Failed to parse svn info for external "+ext.url+" at "+ext.path));
                    }
                }
                return revisions;
            } finally {
                manager.dispose();
            }
        }
        private static final long serialVersionUID = 1L;
    }
    public static File getRevisionFile(AbstractBuild build) {
        return new File(build.getRootDir(),"revision.txt");
    }
    private static File getExternalsFile(AbstractProject project) {
        return new File(project.getRootDir(),"svnexternals.txt");
    }
    @Override
    public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener) throws IOException, InterruptedException {
        Map<String,Long> wsRev = parseRevisionFile(build,true,true);
        return new SVNRevisionState(wsRev);
    }
    private boolean isPollFromMaster() {
        return pollFromMaster;
    }
    void setPollFromMaster(boolean pollFromMaster) {
        this.pollFromMaster = pollFromMaster;
    }
    @Override
    protected PollingResult compareRemoteRevisionWith(AbstractProject<?,?> project, Launcher launcher, FilePath workspace, final TaskListener listener, SCMRevisionState _baseline) throws IOException, InterruptedException {
        final SVNRevisionState baseline;
        if (_baseline instanceof SVNRevisionState) {
            baseline = (SVNRevisionState)_baseline;
        }
        else if (project.getLastBuild()!=null) {
            baseline = (SVNRevisionState)calcRevisionsFromBuild(project.getLastBuild(), launcher, listener);
        }
        else {
            baseline = new SVNRevisionState(null);
        }
        if (project.getLastBuild() == null) {
            listener.getLogger().println(Messages.SubversionSCM_pollChanges_noBuilds());
            return BUILD_NOW;
        }
        AbstractBuild<?,?> lastCompletedBuild = project.getLastCompletedBuild();
        if (lastCompletedBuild!=null) {
            EnvVars env = lastCompletedBuild.getEnvironment(listener);
            EnvVarsUtils.overrideAll(env, lastCompletedBuild.getBuildVariables());
            if (repositoryLocationsNoLongerExist(lastCompletedBuild, listener, env)) {
                listener.getLogger().println(
                        Messages.SubversionSCM_pollChanges_locationsNoLongerExist(project));
                project.makeDisabled(true);
                return NO_CHANGES;
            }
            for (ModuleLocation loc : getLocations(env, lastCompletedBuild)) {
                String url;
                try { 
                    url = loc.getSVNURL().toDecodedString();
                } catch (SVNException ex) {
                    ex.printStackTrace(listener.error(Messages.SubversionSCM_pollChanges_exception(loc.getURL())));
                    return BUILD_NOW;
                }
                if (!baseline.revisions.containsKey(url)) {
                    listener.getLogger().println(
                            Messages.SubversionSCM_pollChanges_locationNotInWorkspace(url));
                    return BUILD_NOW;
                }
            }
        }
        VirtualChannel ch=null;
        Node n = null;
        if (!isPollFromMaster()) {
            n = lastCompletedBuild!=null ? lastCompletedBuild.getBuiltOn() : null;
            if (n!=null) {
                Computer c = n.toComputer();
                if (c!=null)    ch = c.getChannel();
            }
        }
        if (ch==null)   ch= MasterComputer.localChannel;
        final String nodeName = n!=null ? n.getNodeName() : "master";
        final SVNLogHandler logHandler = new SVNLogHandler(createSVNLogFilter(), listener);
        final ISVNAuthenticationProvider authProvider = getDescriptor().createAuthenticationProvider(project);
        return ch.call(new CompareAgainstBaselineCallable(baseline, logHandler, project.getName(), listener, authProvider, nodeName));
    }
    public SVNLogFilter createSVNLogFilter() {
        return new DefaultSVNLogFilter(getExcludedRegionsPatterns(), getIncludedRegionsPatterns(),
                getExcludedUsersNormalized(), getExcludedRevprop(), getExcludedCommitMessagesPatterns(), isIgnoreDirPropChanges());
    }
    static final class SVNLogHandler implements ISVNLogEntryHandler, Serializable {
        private boolean changesFound = false;
        private SVNLogFilter filter;
        SVNLogHandler(SVNLogFilter svnLogFilter, TaskListener listener) {
            this.filter = svnLogFilter;;
            this.filter.setTaskListener(listener);
        }
        public boolean isChangesFound() {
            return changesFound;
        }
        public boolean findNonExcludedChanges(SVNURL url, long from, long to, ISVNAuthenticationProvider authProvider) throws SVNException {
            if (from>to)        return false; 
            if (!filter.hasExclusionRule())    return true;
            final SvnClientManager manager = createClientManager(authProvider);
            try {
                manager.getLogClient().doLog(url, null, SVNRevision.UNDEFINED,
                        SVNRevision.create(from), 
                        SVNRevision.create(to), 
                        false, 
                        true, 
                        false, 
                        0, 
                        null, 
                        this);
            } finally {
                manager.dispose();
            }
            return isChangesFound();
        }
        public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
            if (filter.isIncluded(logEntry)) {
                changesFound = true;
            }
        }
        private static final long serialVersionUID = 1L;
    }
    public ChangeLogParser createChangeLogParser() {
        return new SubversionChangeLogParser(ignoreDirPropChanges);
    }
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
    @Override
    @Deprecated
    public FilePath getModuleRoot(FilePath workspace) {
        if (getLocations().length > 0)
            return workspace.child(getLocations()[0].getLocalDir());
        return workspace;
    }
    @Override
    public FilePath getModuleRoot(FilePath workspace, AbstractBuild build) {
        if (build == null) {
            return getModuleRoot(workspace);
        }
        TaskListener listener = new LogTaskListener(LOGGER, WARNING);
        final EnvVars env;
        try {
            env = build.getEnvironment(listener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        if (getLocations().length > 0)
            return _getModuleRoot(workspace, getLocations()[0].getLocalDir(), env);
        return workspace;
    }
    @Deprecated
    @Override
    public FilePath[] getModuleRoots(FilePath workspace) {
        final ModuleLocation[] moduleLocations = getLocations();
        if (moduleLocations.length > 0) {
            FilePath[] moduleRoots = new FilePath[moduleLocations.length];
            for (int i = 0; i < moduleLocations.length; i++) {
                moduleRoots[i] = workspace.child(moduleLocations[i].getLocalDir());
            }
            return moduleRoots;
        }
        return new FilePath[] { getModuleRoot(workspace) };
    }
    @Override
    public FilePath[] getModuleRoots(FilePath workspace, AbstractBuild build) {
        if (build == null) {
            return getModuleRoots(workspace);
        }
        TaskListener listener = new LogTaskListener(LOGGER, WARNING);
        final EnvVars env;
        try {
            env = build.getEnvironment(listener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        final ModuleLocation[] moduleLocations = getLocations();
        if (moduleLocations.length > 0) {
            FilePath[] moduleRoots = new FilePath[moduleLocations.length];
            for (int i = 0; i < moduleLocations.length; i++) {
                moduleRoots[i] = _getModuleRoot(workspace, moduleLocations[i].getLocalDir(), env);
            }
            return moduleRoots;
        }
        return new FilePath[] { getModuleRoot(workspace, build) };
    }
    FilePath _getModuleRoot(FilePath workspace, String localDir, EnvVars env) {
        return workspace.child(
                env.expand(localDir));
    }
    private static String getLastPathComponent(String s) {
        String[] tokens = s.split("/");
        return tokens[tokens.length-1]; 
    }
    @Extension
    public static class DescriptorImpl extends SCMDescriptor<SubversionSCM> implements hudson.model.ModelObject {
        private final Map<String,Credential> credentials = new Hashtable<String,Credential>();
        private String globalExcludedRevprop = null;
        private int workspaceFormat = SVNAdminAreaFactory.WC_FORMAT_14;
        private boolean validateRemoteUpToVar = false;
        private boolean storeAuthToDisk = true;
        public static abstract class Credential implements Serializable {
            private static final long serialVersionUID = -3707951427730113110L;
            public abstract SVNAuthentication createSVNAuthentication(String kind) throws SVNException;
        }
        public static final class PasswordCredential extends Credential {
            private static final long serialVersionUID = -1676145651108866745L;
            private final String userName;
            private final String password; 
            public PasswordCredential(String userName, String password) {
                this.userName = userName;
                this.password = Scrambler.scramble(password);
            }
            @Override
            public SVNAuthentication createSVNAuthentication(String kind) {
                if(kind.equals(ISVNAuthenticationManager.SSH))
                    return new SVNSSHAuthentication(userName,Scrambler.descramble(password),-1,false);
                else
                    return new SVNPasswordAuthentication(userName,Scrambler.descramble(password),false);
            }
        }
        public static final class SshPublicKeyCredential extends Credential {
            private static final long serialVersionUID = -4649332611621900514L;
            private final String userName;
            private final String passphrase; 
            private final String id;
            public SshPublicKeyCredential(String userName, String passphrase, File keyFile) throws SVNException {
                this.userName = userName;
                this.passphrase = Scrambler.scramble(passphrase);
                Random r = new Random();
                StringBuilder buf = new StringBuilder();
                for(int i=0;i<16;i++)
                    buf.append(Integer.toHexString(r.nextInt(16)));
                this.id = buf.toString();
                try {
                    File savedKeyFile = getKeyFile();
                    FileUtils.copyFile(keyFile,savedKeyFile);
                    setFilePermissions(savedKeyFile, "600");
                } catch (IOException e) {
                    throw new SVNException(
                            SVNErrorMessage.create(SVNErrorCode.AUTHN_CREDS_UNAVAILABLE,"Unable to save private key").initCause(e));
                }
            }
            private File getKeyFile() {
                File dir = new File(Hudson.getInstance().getRootDir(),"subversion-credentials");
                if(dir.mkdirs()) {
                    setFilePermissions(dir, "600");
                }
                return new File(dir,id);
            }
            private boolean setFilePermissions(File file, String perms) {
                try {
                    Chmod chmod = new Chmod();
                    chmod.setProject(new Project());
                    chmod.setFile(file);
                    chmod.setPerm(perms);
                    chmod.execute();
                } catch (BuildException e) {
                    LOGGER.log(Level.WARNING, "Failed to set permission of "+file,e);
                    return false;
                }
                return true;
            }
            @Override
            public SVNSSHAuthentication createSVNAuthentication(String kind) throws SVNException {
                if(kind.equals(ISVNAuthenticationManager.SSH)) {
                    try {
                        Channel channel = Channel.current();
                        String privateKey;
                        if(channel!=null) {
                            privateKey = channel.call(new Callable<String,IOException>() {
                                private static final long serialVersionUID = -3088632649290496373L;
                                public String call() throws IOException {
                                    return FileUtils.readFileToString(getKeyFile(),"iso-8859-1");
                                }
                            });
                        } else {
                            privateKey = FileUtils.readFileToString(getKeyFile(),"iso-8859-1");
                        }
                        return new SVNSSHAuthentication(userName, privateKey.toCharArray(), Scrambler.descramble(passphrase),-1,false);
                    } catch (IOException e) {
                        throw new SVNException(
                                SVNErrorMessage.create(SVNErrorCode.AUTHN_CREDS_UNAVAILABLE,"Unable to load private key").initCause(e));
                    } catch (InterruptedException e) {
                        throw new SVNException(
                                SVNErrorMessage.create(SVNErrorCode.AUTHN_CREDS_UNAVAILABLE,"Unable to load private key").initCause(e));
                    }
                } else
                    return null; 
            }
        }
        public static final class SslClientCertificateCredential extends Credential {
            private static final long serialVersionUID = 5455755079546887446L;
            private final Secret certificate;
            private final String password; 
            public SslClientCertificateCredential(File certificate, String password) throws IOException {
                this.password = Scrambler.scramble(password);
                this.certificate = Secret.fromString(new String(Base64.encode(FileUtils.readFileToByteArray(certificate))));
            }
            @Override
            public SVNAuthentication createSVNAuthentication(String kind) {
                if(kind.equals(ISVNAuthenticationManager.SSL))
                    try {
                        SVNSSLAuthentication authentication = new SVNSSLAuthentication(
                                Base64.decode(certificate.getPlainText().toCharArray()),
                                Scrambler.descramble(password), false);
                        authentication.setCertificatePath("dummy"); 
                        return authentication;
                    } catch (IOException e) {
                        throw new Error(e); 
                    }
                else
                    return null; 
            }
        }
        interface RemotableSVNAuthenticationProvider extends Serializable {
            Credential getCredential(SVNURL url, String realm);
            void acknowledgeAuthentication(String realm, Credential credential);
        }
        private transient final RemotableSVNAuthenticationProviderImpl remotableProvider = new RemotableSVNAuthenticationProviderImpl();
        private final class RemotableSVNAuthenticationProviderImpl implements RemotableSVNAuthenticationProvider {
            private static final long serialVersionUID = 1243451839093253666L;
            public Credential getCredential(SVNURL url, String realm) {
                for (SubversionCredentialProvider p : SubversionCredentialProvider.all()) {
                    Credential c = p.getCredential(url,realm);
                    if(c!=null) {
                        LOGGER.fine(String.format("getCredential(%s)=>%s by %s",realm,c,p));
                        return c;
                    }
                }
                LOGGER.fine(String.format("getCredential(%s)=>%s",realm,credentials.get(realm)));
                return credentials.get(realm);
            }
            public void acknowledgeAuthentication(String realm, Credential credential) {
            }
            private Object writeReplace() {
                return Channel.current().export(RemotableSVNAuthenticationProvider.class, this);
            }
        }
        static final class SVNAuthenticationProviderImpl implements ISVNAuthenticationProvider, ISVNAuthenticationOutcomeListener, Serializable {
            private final RemotableSVNAuthenticationProvider local;
            private final RemotableSVNAuthenticationProvider global;
            private Credential lastCredential;
            public SVNAuthenticationProviderImpl(RemotableSVNAuthenticationProvider local, RemotableSVNAuthenticationProvider global) {
                this.global = global;
                this.local = local;
            }
            private SVNAuthentication fromProvider(SVNURL url, String realm, String kind, RemotableSVNAuthenticationProvider src, String debugName) throws SVNException {
                if (src==null)  return null;
                Credential cred = src.getCredential(url,realm);
                LOGGER.fine(String.format("%s.requestClientAuthentication(%s,%s,%s)=>%s",debugName,kind,url,realm,cred));
                this.lastCredential = cred;
                if(cred!=null)  return cred.createSVNAuthentication(kind);
                return null;
            }
            public SVNAuthentication requestClientAuthentication(String kind, SVNURL url, String realm, SVNErrorMessage errorMessage, SVNAuthentication previousAuth, boolean authMayBeStored) {
                try {
                    SVNAuthentication auth=fromProvider(url,realm,kind,local,"local");
                    if (auth==null || compareSVNAuthentications(auth,previousAuth))
                        auth = fromProvider(url,realm,kind,global,"global");
                    if(previousAuth!=null && compareSVNAuthentications(auth,previousAuth)) {
                        LOGGER.log(FINE, "Previous authentication attempt failed, so aborting: {0}", previousAuth);
                        return null;
                    }
                    if(auth==null && ISVNAuthenticationManager.USERNAME.equals(kind)) {
                        return new SVNUserNameAuthentication("",false);
                    }
                    return auth;
                } catch (SVNException e) {
                    LOGGER.log(Level.SEVERE, "Failed to authorize",e);
                    throw new RuntimeException("Failed to authorize",e);
                }
            }
            public void acknowledgeAuthentication(boolean accepted, String kind, String realm, SVNErrorMessage errorMessage, SVNAuthentication authentication) throws SVNException {
                if (accepted && local!=null && lastCredential!=null)
                    local.acknowledgeAuthentication(realm,lastCredential);
            }
            public int acceptServerAuthentication(SVNURL url, String realm, Object certificate, boolean resultMayBeStored) {
                return ACCEPTED_TEMPORARY;
            }
            private static final long serialVersionUID = 1L;
        }
        @Override
        public SCM newInstance(StaplerRequest staplerRequest, JSONObject jsonObject) throws FormException {
            return super.newInstance(staplerRequest, jsonObject);
        }
        public DescriptorImpl() {
            super(SubversionRepositoryBrowser.class);
            load();
        }
        @SuppressWarnings("unchecked")
        protected DescriptorImpl(Class clazz, Class<? extends RepositoryBrowser> repositoryBrowser) {
            super(clazz,repositoryBrowser);
        }
        public String getDisplayName() {
            return "Subversion";
        }
        public String getGlobalExcludedRevprop() {
            return globalExcludedRevprop;
        }
        public int getWorkspaceFormat() {
            if (workspaceFormat==0)
                return SVNAdminAreaFactory.WC_FORMAT_14; 
            return workspaceFormat;
        }
        public boolean isValidateRemoteUpToVar() {
            return validateRemoteUpToVar;
        }
        public boolean isStoreAuthToDisk() {
            return storeAuthToDisk;
        }
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            globalExcludedRevprop = fixEmptyAndTrim(
                    req.getParameter("svn.global_excluded_revprop"));
            workspaceFormat = Integer.parseInt(req.getParameter("svn.workspaceFormat"));
            validateRemoteUpToVar = formData.containsKey("validateRemoteUpToVar");
            storeAuthToDisk = formData.containsKey("storeAuthToDisk");
            save();
            return super.configure(req, formData);
        }
        @Override
        public boolean isBrowserReusable(SubversionSCM x, SubversionSCM y) {
            ModuleLocation[] xl = x.getLocations(), yl = y.getLocations();
            if (xl.length != yl.length) return false;
            for (int i = 0; i < xl.length; i++)
                if (!xl[i].getURL().equals(yl[i].getURL())) return false;
            return true;
        }
        public ISVNAuthenticationProvider createAuthenticationProvider(AbstractProject<?,?> inContextOf) {
            return new SVNAuthenticationProviderImpl(
                    inContextOf==null ? null : new PerJobCredentialStore(inContextOf),remotableProvider);
        }
        public ISVNAuthenticationProvider createAuthenticationProvider() {
            return new SVNAuthenticationProviderImpl(null,remotableProvider);
        }
        public void doPostCredential(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
            Hudson.getInstance().checkPermission(Item.CONFIGURE);
            MultipartFormDataParser parser = new MultipartFormDataParser(req);
            StringWriter log = new StringWriter();
            PrintWriter logWriter = new PrintWriter(log);
            UserProvidedCredential upc = UserProvidedCredential.fromForm(req,parser);
            try {
                postCredential(parser.get("url"), upc, logWriter);
                rsp.sendRedirect("credentialOK");
            } catch (SVNException e) {
                logWriter.println("FAILED: "+e.getErrorMessage());
                req.setAttribute("message",log.toString());
                req.setAttribute("pre",true);
                req.setAttribute("exception",e);
                rsp.forward(Hudson.getInstance(),"error",req);
            } finally {
                upc.close();
            }
        }
        public void postCredential(String url, String username, String password, File keyFile, PrintWriter logWriter) throws SVNException, IOException {
            postCredential(null,url,username,password,keyFile,logWriter);
        }
        public void postCredential(AbstractProject inContextOf, String url, String username, String password, File keyFile, PrintWriter logWriter) throws SVNException, IOException {
            postCredential(url,new UserProvidedCredential(username,password,keyFile,inContextOf),logWriter);
        }
        public void postCredential(String url, final UserProvidedCredential upc, PrintWriter logWriter) throws SVNException, IOException {
            SVNRepository repository = null;
            try {
                repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
                repository.setTunnelProvider( createDefaultSVNOptions() );
                AuthenticationManagerImpl authManager = upc.new AuthenticationManagerImpl(logWriter) {
                    @Override
                    protected void onSuccess(String realm, Credential cred) {
                        LOGGER.info("Persisted "+cred+" for "+realm);
                        credentials.put(realm, cred);
                        save();
                        if (upc.inContextOf!=null)
                            new PerJobCredentialStore(upc.inContextOf).acknowledgeAuthentication(realm,cred);
                    }
                };
                authManager.setAuthenticationForced(true);
                repository.setAuthenticationManager(authManager);
                repository.testConnection();
                authManager.checkIfProtocolCompleted();
            } finally {
                if (repository != null)
                    repository.closeSession();
            }
        }
        public FormValidation doCheckRemote(StaplerRequest req, @AncestorInPath AbstractProject context, @QueryParameter String value) {
            String url = Util.fixEmptyAndTrim(value);
            if (url == null)
                return FormValidation.error(Messages.SubversionSCM_doCheckRemote_required()); 
            if(isValidateRemoteUpToVar()) {
                url = (url.indexOf('$') != -1) ? url.substring(0, url.indexOf('$')) : url;
            } else {
                url = new EnvVars(EnvVars.masterEnvVars).expand(url);
            }
            if(!URL_PATTERN.matcher(url).matches())
                return FormValidation.errorWithMarkup(
                    Messages.SubversionSCM_doCheckRemote_invalidUrl());
            if (!Hudson.getInstance().hasPermission(Item.CONFIGURE))
                return FormValidation.ok();
            try {
                String urlWithoutRevision = SvnHelper.getUrlWithoutRevision(url);
                SVNURL repoURL = SVNURL.parseURIDecoded(urlWithoutRevision);
                if (checkRepositoryPath(context,repoURL)!=SVNNodeKind.NONE) {
                    SVNRevision revision = getRevisionFromRemoteUrl(url);
                    if (revision != null && !revision.isValid()) {
                        return FormValidation.errorWithMarkup(Messages.SubversionSCM_doCheckRemote_invalidRevision());
                    }
                    return FormValidation.ok();
                }
                SVNRepository repository = null;
                try {
                    repository = getRepository(context,repoURL);
                    long rev = repository.getLatestRevision();
                    String repoPath = getRelativePath(repoURL, repository);
                    String p = repoPath;
                    while(p.length()>0) {
                        p = SVNPathUtil.removeTail(p);
                        if(repository.checkPath(p,rev)==SVNNodeKind.DIR) {
                            List<SVNDirEntry> entries = new ArrayList<SVNDirEntry>();
                            repository.getDir(p,rev,false,entries);
                            List<String> paths = new ArrayList<String>();
                            for (SVNDirEntry e : entries)
                                if(e.getKind()==SVNNodeKind.DIR)
                                    paths.add(e.getName());
                            String head = SVNPathUtil.head(repoPath.substring(p.length() + 1));
                            String candidate = EditDistance.findNearest(head,paths);
                            return FormValidation.error(
                                Messages.SubversionSCM_doCheckRemote_badPathSuggest(p, head,
                                    candidate != null ? "/" + candidate : ""));
                        }
                    }
                    return FormValidation.error(
                        Messages.SubversionSCM_doCheckRemote_badPath(repoPath));
                } finally {
                    if (repository != null)
                        repository.closeSession();
                }
            } catch (SVNException e) {
                LOGGER.log(Level.INFO, "Failed to access subversion repository "+url,e);
                String message = Messages.SubversionSCM_doCheckRemote_exceptionMsg1(
                    Util.escape(url), Util.escape(e.getErrorMessage().getFullMessage()),
                    "javascript:document.getElementById('svnerror').style.display='block';"
                      + "document.getElementById('svnerrorlink').style.display='none';"
                      + "return false;")
                  + "<br/><pre id=\"svnerror\" style=\"display:none\">"
                  + Functions.printThrowable(e) + "</pre>"
                  + Messages.SubversionSCM_doCheckRemote_exceptionMsg2(
                      "descriptorByName/"+SubversionSCM.class.getName()+"/enterCredential?" + url);
                return FormValidation.errorWithMarkup(message);
            }
        }
        public SVNNodeKind checkRepositoryPath(AbstractProject context, SVNURL repoURL) throws SVNException {
            SVNRepository repository = null;
            try {
                repository = getRepository(context,repoURL);
                repository.testConnection();
                long rev = repository.getLatestRevision();
                String repoPath = getRelativePath(repoURL, repository);
                return repository.checkPath(repoPath, rev);
            } finally {
                if (repository != null)
                    repository.closeSession();
            }
        }
        protected SVNRepository getRepository(AbstractProject context, SVNURL repoURL) throws SVNException {
            SVNRepository repository = SVNRepositoryFactory.create(repoURL);
            ISVNAuthenticationManager sam = createSvnAuthenticationManager(createAuthenticationProvider(context));
            sam = new FilterSVNAuthenticationManager(sam) {
                @Override
                public int getReadTimeout(SVNRepository repository) {
                    int r = super.getReadTimeout(repository);
                    if(r<=0)    r = DEFAULT_TIMEOUT;
                    return r;
                }
            };
            repository.setTunnelProvider(createDefaultSVNOptions());
            repository.setAuthenticationManager(sam);
            return repository;
        }
        public static String getRelativePath(SVNURL repoURL, SVNRepository repository) throws SVNException {
            String repoPath = repoURL.getPath().substring(repository.getRepositoryRoot(false).getPath().length());
            if(!repoPath.startsWith("/"))    repoPath="/"+repoPath;
            return repoPath;
        }
        public FormValidation doCheckLocal(@QueryParameter String value) throws IOException, ServletException {
            String v = Util.nullify(value);
            if (v == null)
                return FormValidation.ok();
            v = v.trim();
            if (v.startsWith("/") || v.startsWith("\\") || v.startsWith("..") || v.matches("^[A-Za-z]:.*"))
                return FormValidation.error("absolute path is not allowed");
            return FormValidation.ok();
        }
        public FormValidation doCheckExcludedRegions(@QueryParameter String value) throws IOException, ServletException {
            for (String region : Util.fixNull(value).trim().split("[\\r\\n]+"))
                try {
                    Pattern.compile(region);
                } catch (PatternSyntaxException e) {
                    return FormValidation.error("Invalid regular expression. " + e.getMessage());
                }
            return FormValidation.ok();
        }
        public FormValidation doCheckIncludedRegions(@QueryParameter String value) throws IOException, ServletException {
            return  doCheckExcludedRegions(value);
        }
        private static final Pattern USERNAME_PATTERN = Pattern.compile("(\\w+\\\\)?+(\\w+)");
        public FormValidation doCheckExcludedUsers(@QueryParameter String value) throws IOException, ServletException {
            for (String user : Util.fixNull(value).trim().split("[\\r\\n]+")) {
                user = user.trim();
                if ("".equals(user)) {
                    continue;
                }
                if (!USERNAME_PATTERN.matcher(user).matches()) {
                    return FormValidation.error("Invalid username: " + user);
                }
            }
            return FormValidation.ok();
        }
        public List<WorkspaceUpdaterDescriptor> getWorkspaceUpdaterDescriptors() {
            return WorkspaceUpdaterDescriptor.all();
        }
        public FormValidation doCheckExcludedCommitMessages(@QueryParameter String value) throws IOException, ServletException {
            for (String message : Util.fixNull(value).trim().split("[\\r\\n]+")) {
                try {
                    Pattern.compile(message);
                } catch (PatternSyntaxException e) {
                    return FormValidation.error("Invalid regular expression. " + e.getMessage());
                }
            }
            return FormValidation.ok();
        }
        public FormValidation doCheckRevisionPropertiesSupported(@AncestorInPath AbstractProject context, @QueryParameter String value) throws IOException, ServletException {
            String v = Util.fixNull(value).trim();
            if (v.length() == 0)
                return FormValidation.ok();
            if (!Hudson.getInstance().hasPermission(Hudson.ADMINISTER))
                return FormValidation.ok();
            try {
                SVNURL repoURL = SVNURL.parseURIDecoded(new EnvVars(EnvVars.masterEnvVars).expand(v));
                if (checkRepositoryPath(context,repoURL)!=SVNNodeKind.NONE)
                    return FormValidation.ok();
                SVNRepository repository = null;
                try {
                    repository = getRepository(context,repoURL);
                    if (repository.hasCapability(SVNCapability.LOG_REVPROPS))
                        return FormValidation.ok();
                } finally {
                    if (repository != null)
                        repository.closeSession();
                }
            } catch (SVNException e) {
                String message="";
                message += "Unable to access "+Util.escape(v)+" : "+Util.escape( e.getErrorMessage().getFullMessage());
                LOGGER.log(Level.INFO, "Failed to access subversion repository "+v,e);
                return FormValidation.errorWithMarkup(message);
            }
            return FormValidation.warning(Messages.SubversionSCM_excludedRevprop_notSupported(v));
        }
        static {
            new Initializer();
        }
    }
    public boolean repositoryLocationsNoLongerExist(AbstractBuild<?,?> build, TaskListener listener) {
        return repositoryLocationsNoLongerExist(build, listener, null);
    }
    public boolean repositoryLocationsNoLongerExist(AbstractBuild<?,?> build, TaskListener listener, EnvVars env) {
        PrintStream out = listener.getLogger();
        for (ModuleLocation l : getLocations(env, build))
            try {
                if (getDescriptor().checkRepositoryPath(build.getProject(), l.getSVNURL()) == SVNNodeKind.NONE) {
                    out.println("Location '" + l.remote + "' does not exist");
                    ParametersAction params = build.getAction(ParametersAction.class);
                    if (params != null) {
                        LOGGER.fine("Location could be expanded on build '" + build
                                + "' parameters values:");
                        return false;
                    }
                    return true;
                }
            } catch (SVNException e) {
                LOGGER.log(FINE, "Location check failed",e);
            }
        return false;
    }
    static final Pattern URL_PATTERN = Pattern.compile("(https?|svn(\\+[a-z0-9]+)?|file):
    private static final long serialVersionUID = 1L;
    public static void init() {}
    static {
        new Initializer();
    }
    private static final class Initializer {
        static {
            if(Boolean.getBoolean("hudson.spool-svn"))
                DAVRepositoryFactory.setup(new DefaultHTTPConnectionFactory(null,true,null));
            else
                DAVRepositoryFactory.setup();   
            SVNRepositoryFactoryImpl.setup();   
            FSRepositoryFactory.setup();    
            if(System.getProperty("svnkit.ssh2.persistent")==null)
                System.setProperty("svnkit.ssh2.persistent","false");
            if(System.getProperty("svnkit.http.methods")==null)
                System.setProperty("svnkit.http.methods","Digest,Basic,NTLM,Negotiate");
            SVNAdminAreaFactory.setSelector(new SubversionWorkspaceSelector());
        }
    }
    @ExportedBean
    public static final class ModuleLocation implements Serializable {
        @Exported
        public final String remote;
        @Exported
        public final String local;
        @Exported
        public final String depthOption;
        @Exported
        public boolean ignoreExternalsOption;
        private transient volatile UUID repositoryUUID;
        private transient volatile SVNURL repositoryRoot;
        public ModuleLocation(String remote, String local) {
            this(remote, local, null, false);
        }
        @DataBoundConstructor
        public ModuleLocation(String remote, String local, String depthOption, boolean ignoreExternalsOption) {
            this.remote = Util.removeTrailingSlash(Util.fixNull(remote).trim());
            this.local = fixEmptyAndTrim(local);
            this.depthOption = StringUtils.isEmpty(depthOption) ? SVNDepth.INFINITY.getName() : depthOption;
            this.ignoreExternalsOption = ignoreExternalsOption;
        }
        public String getLocalDir() {
            if(local==null) 
                return getLastPathComponent(getURL());
            return local;
        }
        public String getURL() {
        	return SvnHelper.getUrlWithoutRevision(remote);
        }
        public SVNURL getSVNURL() throws SVNException {
            return SVNURL.parseURIEncoded(getURL());
        }
        public UUID getUUID(AbstractProject context) throws SVNException {
            if(repositoryUUID==null || repositoryRoot==null) {
                synchronized (this) {
                    SVNRepository r = openRepository(context);
                    r.testConnection(); 
                    repositoryUUID = UUID.fromString(r.getRepositoryUUID(false));
                    repositoryRoot = r.getRepositoryRoot(false);
                }
            }
            return repositoryUUID;
        }
        public SVNRepository openRepository(AbstractProject context) throws SVNException {
            return Hudson.getInstance().getDescriptorByType(DescriptorImpl.class).getRepository(context,getSVNURL());
        }
        public SVNURL getRepositoryRoot(AbstractProject context) throws SVNException {
            getUUID(context);
            return repositoryRoot;
        }
        public SVNRevision getRevision(SVNRevision defaultValue) {
            SVNRevision revision = getRevisionFromRemoteUrl(remote);
            return revision != null ? revision : defaultValue;
        }
        public String getDepthOption() {
            return depthOption;
        }
        public boolean isIgnoreExternalsOption() {
            return ignoreExternalsOption;
        }
        public ModuleLocation getExpandedLocation(AbstractBuild<?, ?> build) {
            EnvVars env = new EnvVars(EnvVars.masterEnvVars);
            env.putAll(build.getBuildVariables());
            return getExpandedLocation(env);
        }
        public ModuleLocation getExpandedLocation(EnvVars env) {
            return new ModuleLocation(env.expand(remote), env.expand(getLocalDir()), getDepthOption(), isIgnoreExternalsOption());
        }
        @Override
        public String toString() {
            return remote;
        }
        private static final long serialVersionUID = 1L;
        public static List<ModuleLocation> parse(String[] remoteLocations, String[] localLocations, String[] depthOptions, boolean[] isIgnoreExternals) {
            List<ModuleLocation> modules = new ArrayList<ModuleLocation>();
            if (remoteLocations != null && localLocations != null) {
                int entries = Math.min(remoteLocations.length, localLocations.length);
                for (int i = 0; i < entries; i++) {
                    String remoteLoc = Util.nullify(remoteLocations[i]);
                    if (remoteLoc != null) {
                        remoteLoc = Util.removeTrailingSlash(remoteLoc.trim());
                        modules.add(new ModuleLocation(remoteLoc, Util.nullify(localLocations[i]),
                            depthOptions != null ? depthOptions[i] : null,
                            isIgnoreExternals != null && isIgnoreExternals[i]));
                    }
                }
            }
            return modules;
        }
    }
    private static final Logger LOGGER = Logger.getLogger(SubversionSCM.class.getName());
    public static int DEFAULT_TIMEOUT = Integer.getInteger(SubversionSCM.class.getName()+".timeout",3600*1000);
    private static boolean POLL_FROM_MASTER = Boolean.getBoolean(SubversionSCM.class.getName()+".pollFromMaster");
    public static String CONFIG_DIR = System.getProperty(SubversionSCM.class.getName()+".configDir");
    public static void enableSshDebug(Level level) {
        if(level==null)     level= Level.FINEST; 
        final Level lv = level;
        com.trilead.ssh2.log.Logger.enabled=true;
        com.trilead.ssh2.log.Logger.logger = new DebugLogger() {
            private final Logger LOGGER = Logger.getLogger(SCPClient.class.getPackage().getName());
            public void log(int level, String className, String message) {
                LOGGER.log(lv,className+' '+message);
            }
        };
    }
     static boolean compareSVNAuthentications(SVNAuthentication a1, SVNAuthentication a2) {
        if (a1==null && a2==null)       return true;
        if (a1==null || a2==null)       return false;
        if (a1.getClass()!=a2.getClass())    return false;
        try {
            return describeBean(a1).equals(describeBean(a2));
        } catch (IllegalAccessException e) {
            return false;
        } catch (InvocationTargetException e) {
            return false;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
    @SuppressWarnings("unchecked")
    private static Map describeBean(Object o) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<?,?> m = PropertyUtils.describe(o);
        for (Entry e : m.entrySet()) {
            Object v = e.getValue();
            if (v instanceof char[]) {
                char[] chars = (char[]) v;
                e.setValue(new String(chars));
            }
        }
        return m;
    }
    private static SVNRevision getRevisionFromRemoteUrl(
            String remoteUrlPossiblyWithRevision) {
        int idx = remoteUrlPossiblyWithRevision.lastIndexOf('@');
        int slashIdx = remoteUrlPossiblyWithRevision.lastIndexOf('/');
        if (idx > 0 && idx > slashIdx) {
            String n = remoteUrlPossiblyWithRevision.substring(idx + 1);
            return SVNRevision.parse(n);
        }
        return null;
    }
}
