
package hudson.model;
import com.google.common.base.Predicate;
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import hudson.*;
import hudson.model.Descriptor.FormException;
import hudson.model.listeners.SaveableListener;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import hudson.security.SecurityRealm;
import hudson.security.UserMayOrMayNotExistException;
import hudson.util.FormApply;
import hudson.util.FormValidation;
import hudson.util.RunList;
import hudson.util.XStream2;
import jenkins.model.IdStrategy;
import jenkins.model.Jenkins;
import jenkins.model.ModelObjectWithContextMenu;
import jenkins.security.ImpersonatingUserDetailsService;
import jenkins.security.LastGrantedAuthoritiesProperty;
import net.sf.json.JSONObject;
import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.kohsuke.stapler.interceptor.RequirePOST;
import javax.annotation.concurrent.GuardedBy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
@ExportedBean
public class User extends AbstractModelObject implements AccessControlled, DescriptorByNameOwner, Saveable, Comparable<User>, ModelObjectWithContextMenu {
    private static final String UKNOWN_USERNAME = "unknown";
    private static final String[] ILLEGAL_PERSISTED_USERNAMES = new String[]{ACL.ANONYMOUS_USERNAME,
            ACL.SYSTEM_USERNAME, UKNOWN_USERNAME};
    private transient final String id;
    private volatile String fullName;
    private volatile String description;
    @CopyOnWrite
    private volatile List<UserProperty> properties = new ArrayList<UserProperty>();
    private User(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
        load();
    }
    @Nonnull
    public static IdStrategy idStrategy() {
        Jenkins j = Jenkins.getInstance();
        if (j == null) {
            return IdStrategy.CASE_INSENSITIVE;
        }
        SecurityRealm realm = j.getSecurityRealm();
        if (realm == null) {
            return IdStrategy.CASE_INSENSITIVE;
        }
        return realm.getUserIdStrategy();
    }
    public int compareTo(User that) {
        return idStrategy().compare(this.id, that.id);
    }
    private synchronized void load() {
        properties.clear();
        XmlFile config = getConfigFile();
        try {
            if(config.exists())
                config.unmarshal(this);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load "+config,e);
        }
        for (Iterator<UserProperty> itr = properties.iterator(); itr.hasNext();) {
            if(itr.next()==null)
                itr.remove();            
        }
        for (UserPropertyDescriptor d : UserProperty.all()) {
            if(getProperty(d.clazz)==null) {
                UserProperty up = d.newInstance(this);
                if(up!=null)
                    properties.add(up);
            }
        }
        for (UserProperty p : properties)
            p.setUser(this);
    }
    @Exported
    public String getId() {
        return id;
    }
    public @Nonnull String getUrl() {
        return "user/"+Util.rawEncode(idStrategy().keyFor(id));
    }
    public @Nonnull String getSearchUrl() {
        return "/user/"+Util.rawEncode(idStrategy().keyFor(id));
    }
    @Exported(visibility=999)
    public @Nonnull String getAbsoluteUrl() {
        return Jenkins.getInstance().getRootUrl()+getUrl();
    }
    @Exported(visibility=999)
    public @Nonnull String getFullName() {
        return fullName;
    }
    public void setFullName(String name) {
        if(Util.fixEmptyAndTrim(name)==null)    name=id;
        this.fullName = name;
    }
    @Exported
    public @CheckForNull String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Map<Descriptor<UserProperty>,UserProperty> getProperties() {
        return Descriptor.toMap(properties);
    }
    public synchronized void addProperty(@Nonnull UserProperty p) throws IOException {
        UserProperty old = getProperty(p.getClass());
        List<UserProperty> ps = new ArrayList<UserProperty>(properties);
        if(old!=null)
            ps.remove(old);
        ps.add(p);
        p.setUser(this);
        properties = ps;
        save();
    }
    @Exported(name="property",inline=true)
    public List<UserProperty> getAllProperties() {
        return Collections.unmodifiableList(properties);
    }
    public <T extends UserProperty> T getProperty(Class<T> clazz) {
        for (UserProperty p : properties) {
            if(clazz.isInstance(p))
                return clazz.cast(p);
        }
        return null;
    }
    public @Nonnull Authentication impersonate() throws UsernameNotFoundException {
        try {
            UserDetails u = new ImpersonatingUserDetailsService(
                    Jenkins.getInstance().getSecurityRealm().getSecurityComponents().userDetails).loadUserByUsername(id);
            return new UsernamePasswordAuthenticationToken(u.getUsername(), "", u.getAuthorities());
        } catch (UserMayOrMayNotExistException e) {
        } catch (UsernameNotFoundException e) {
            if (!ALLOW_NON_EXISTENT_USER_TO_LOGIN)
                throw e;
        } catch (DataAccessException e) {
        }
        return new UsernamePasswordAuthenticationToken(id, "",
            new GrantedAuthority[]{SecurityRealm.AUTHENTICATED_AUTHORITY});
    }
    public synchronized void doSubmitDescription( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        checkPermission(Jenkins.ADMINISTER);
        description = req.getParameter("description");
        save();
        rsp.sendRedirect(".");  
    }
    public static @Nonnull User getUnknown() {
        return getById(UKNOWN_USERNAME, true);
    }
    @Deprecated
    public static @Nullable User get(String idOrFullName, boolean create) {
        return get(idOrFullName, create, Collections.emptyMap());
    }
    public static @Nullable User get(String idOrFullName, boolean create, Map context) {
        if(idOrFullName==null)
            return null;
        List<CanonicalIdResolver> resolvers = new ArrayList<CanonicalIdResolver>(ExtensionList.lookup(CanonicalIdResolver.class));
        Collections.sort(resolvers);
        String id = null;
        for (CanonicalIdResolver resolver : resolvers) {
            id = resolver.resolveCanonicalId(idOrFullName, context);
            if (id != null) {
                LOGGER.log(Level.FINE, "{0} mapped {1} to {2}", new Object[] {resolver, idOrFullName, id});
                break;
            }
        }
        if (id == null) {
            throw new IllegalStateException("The user id should be always non-null thanks to DefaultUserCanonicalIdResolver");
        }
        return getOrCreate(id, idOrFullName, create);
    }
    private static @Nullable User getOrCreate(@Nonnull String id, @Nonnull String fullName, boolean create) {
        String idkey = idStrategy().keyFor(id);
        byNameLock.readLock().lock();
        User u;
        try {
            u = byName.get(idkey);
        } finally {
            byNameLock.readLock().unlock();
        }
        final File configFile = getConfigFileFor(id);
        if (!configFile.isFile() && !configFile.getParentFile().isDirectory()) {
            File[] legacy = getLegacyConfigFilesFor(id);
            if (legacy != null && legacy.length > 0) {
                for (File legacyUserDir : legacy) {
                    final XmlFile legacyXml = new XmlFile(XSTREAM, new File(legacyUserDir, "config.xml"));
                    try {
                        Object o = legacyXml.read();
                        if (o instanceof User) {
                            if (idStrategy().equals(id, legacyUserDir.getName()) && !idStrategy().filenameOf(legacyUserDir.getName())
                                    .equals(legacyUserDir.getName())) {
                                if (!legacyUserDir.renameTo(configFile.getParentFile())) {
                                    LOGGER.log(Level.WARNING, "Failed to migrate user record from {0} to {1}",
                                            new Object[]{legacyUserDir, configFile.getParentFile()});
                                }
                                break;
                            }
                        } else {
                            LOGGER.log(Level.FINE, "Unexpected object loaded from {0}: {1}",
                                    new Object[]{ legacyUserDir, o });
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.FINE, String.format("Exception trying to load user from {0}: {1}",
                                new Object[]{ legacyUserDir, e.getMessage() }), e);
                    }
                }
            }
        }
        if (u==null && (create || configFile.exists())) {
            User tmp = new User(id, fullName);
            User prev;
            byNameLock.readLock().lock();
            try {
                prev = byName.putIfAbsent(idkey, u = tmp);
            } finally {
                byNameLock.readLock().unlock();
            }
            if (prev != null) {
                u = prev; 
                if (LOGGER.isLoggable(Level.FINE) && !fullName.equals(prev.getFullName())) {
                    LOGGER.log(Level.FINE, "mismatch on fullName (‘" + fullName + "’ vs. ‘" + prev.getFullName() + "’) for ‘" + id + "’", new Throwable());
                }
            } else if (!id.equals(fullName) && !configFile.exists()) {
                try {
                    u.save();
                } catch (IOException x) {
                    LOGGER.log(Level.WARNING, null, x);
                }
            }
        }
        return u;
    }
    public static @Nonnull User get(String idOrFullName) {
        return get(idOrFullName,true);
    }
    public static @CheckForNull User current() {
        return get(Jenkins.getAuthentication());
    }
    public static @CheckForNull User get(@CheckForNull Authentication a) {
        if(a == null || a instanceof AnonymousAuthenticationToken)
            return null;
        String id = a.getName();
        return getById(id, true);
    }
    public static @Nullable User getById(String id, boolean create) {
        return getOrCreate(id, id, create);
    }
    private static volatile long lastScanned;
    public static @Nonnull Collection<User> getAll() {
        final IdStrategy strategy = idStrategy();
        if(System.currentTimeMillis() -lastScanned>10000) {
            lastScanned = System.currentTimeMillis();
            File[] subdirs = getRootDir().listFiles((FileFilter)DirectoryFileFilter.INSTANCE);
            if(subdirs==null)       return Collections.emptyList(); 
            for (File subdir : subdirs)
                if(new File(subdir,"config.xml").exists()) {
                    String name = strategy.idFromFilename(subdir.getName());
                    User.getOrCreate(name, name, true);
                }
            lastScanned = System.currentTimeMillis();
        }
        byNameLock.readLock().lock();
        ArrayList<User> r;
        try {
            r = new ArrayList<User>(byName.values());
        } finally {
            byNameLock.readLock().unlock();
        }
        Collections.sort(r,new Comparator<User>() {
            public int compare(User o1, User o2) {
                return strategy.compare(o1.getId(), o2.getId());
            }
        });
        return r;
    }
    public static void reload() {
        byNameLock.readLock().lock();
        try {
            for (User u : byName.values()) {
                u.load();
            }
        } finally {
            byNameLock.readLock().unlock();
        }
    }
    public static void clear() {
        byNameLock.writeLock().lock();
        try {
            byName.clear();
        } finally {
            byNameLock.writeLock().unlock();
        }
    }
    public static void rekey() {
        final IdStrategy strategy = idStrategy();
        byNameLock.writeLock().lock();
        try {
            for (Map.Entry<String, User> e : byName.entrySet()) {
                String idkey = strategy.keyFor(e.getValue().id);
                if (!idkey.equals(e.getKey())) {
                    byName.remove(e.getKey());
                    byName.putIfAbsent(idkey, e.getValue());
                }
            }
        } finally {
            byNameLock.writeLock().unlock();
        }
    }
    public @Nonnull String getDisplayName() {
        return getFullName();
    }
    private boolean relatedTo(@Nonnull AbstractBuild<?,?> b) {
        if (b.hasParticipant(this)) {
            return true;
        }
        for (Cause cause : b.getCauses()) {
            if (cause instanceof Cause.UserIdCause) {
                String userId = ((Cause.UserIdCause) cause).getUserId();
                if (userId != null && idStrategy().equals(userId, getId())) {
                    return true;
                }
            }
        }
        return false;
    }
    @WithBridgeMethods(List.class)
    public @Nonnull RunList getBuilds() {
    	return new RunList<Run<?,?>>(Jenkins.getInstance().getAllItems(Job.class)).filter(new Predicate<Run<?,?>>() {
            @Override public boolean apply(Run<?,?> r) {
                return r instanceof AbstractBuild && relatedTo((AbstractBuild<?,?>) r);
            }
        });
    }
    public @Nonnull Set<AbstractProject<?,?>> getProjects() {
        Set<AbstractProject<?,?>> r = new HashSet<AbstractProject<?,?>>();
        for (AbstractProject<?,?> p : Jenkins.getInstance().getAllItems(AbstractProject.class))
            if(p.hasParticipant(this))
                r.add(p);
        return r;
    }
    public @Override String toString() {
        return fullName;
    }
    protected final XmlFile getConfigFile() {
        return new XmlFile(XSTREAM,getConfigFileFor(id));
    }
    private static final File getConfigFileFor(String id) {
        return new File(getRootDir(), idStrategy().filenameOf(id) +"/config.xml");
    }
    private static final File[] getLegacyConfigFilesFor(final String id) {
        return getRootDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && new File(pathname, "config.xml").isFile() && idStrategy().equals(
                        pathname.getName(), id);
            }
        });
    }
    private static File getRootDir() {
        return new File(Jenkins.getInstance().getRootDir(), "users");
    }
    public static boolean isIdOrFullnameAllowed(String id) {
        for (String invalidId : ILLEGAL_PERSISTED_USERNAMES) {
            if (id.equalsIgnoreCase(invalidId))
                return false;
        }
        return true;
    }
    public synchronized void save() throws IOException, FormValidation {
        if (! isIdOrFullnameAllowed(id)) {
            throw FormValidation.error(Messages.User_IllegalUsername(id));
        }
        if (! isIdOrFullnameAllowed(fullName)) {
            throw FormValidation.error(Messages.User_IllegalFullname(fullName));
        }
        if(BulkChange.contains(this))   return;
        getConfigFile().write(this);
        SaveableListener.fireOnChange(this, getConfigFile());
    }
    public synchronized void delete() throws IOException {
        final IdStrategy strategy = idStrategy();
        byNameLock.readLock().lock();
        try {
            byName.remove(strategy.keyFor(id));
        } finally {
            byNameLock.readLock().unlock();
        }
        Util.deleteRecursive(new File(getRootDir(), strategy.filenameOf(id)));
    }
    public Api getApi() {
        return new Api(this);
    }
    @RequirePOST
    public void doConfigSubmit( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException, FormException {
        checkPermission(Jenkins.ADMINISTER);
        JSONObject json = req.getSubmittedForm();
        fullName = json.getString("fullName");
        description = json.getString("description");
        List<UserProperty> props = new ArrayList<UserProperty>();
        int i = 0;
        for (UserPropertyDescriptor d : UserProperty.all()) {
            UserProperty p = getProperty(d.clazz);
            JSONObject o = json.optJSONObject("userProperty" + (i++));
            if (o!=null) {
                if (p != null) {
                    p = p.reconfigure(req, o);
                } else {
                    p = d.newInstance(req, o);
                }
                p.setUser(this);
            }
            if (p!=null)
                props.add(p);
        }
        this.properties = props;
        save();
        FormApply.success(".").generateResponse(req,rsp,this);
    }
    @RequirePOST
    public void doDoDelete(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        checkPermission(Jenkins.ADMINISTER);
        if (idStrategy().equals(id, Jenkins.getAuthentication().getName())) {
            rsp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cannot delete self");
            return;
        }
        delete();
        rsp.sendRedirect2("../..");
    }
    public void doRssAll(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        rss(req, rsp, " all builds", getBuilds(), Run.FEED_ADAPTER);
    }
    public void doRssFailed(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        rss(req, rsp, " regression builds", getBuilds().regressionOnly(), Run.FEED_ADAPTER);
    }
    public void doRssLatest(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        final List<Run> lastBuilds = new ArrayList<Run>();
        for (AbstractProject<?,?> p : Jenkins.getInstance().getAllItems(AbstractProject.class)) {
            for (AbstractBuild<?,?> b = p.getLastBuild(); b != null; b = b.getPreviousBuild()) {
                if (relatedTo(b)) {
                    lastBuilds.add(b);
                    break;
                }
            }
        }
        rss(req, rsp, " latest build", RunList.fromRuns(lastBuilds), Run.FEED_ADAPTER_LATEST);
    }
    private void rss(StaplerRequest req, StaplerResponse rsp, String suffix, RunList runs, FeedAdapter adapter)
            throws IOException, ServletException {
        RSS.forwardToRss(getDisplayName()+ suffix, getUrl(), runs.newBuilds(), adapter, req, rsp);
    }
    @GuardedBy("byNameLock")
    private static final ConcurrentMap<String,User> byName = new ConcurrentHashMap<String, User>();
    private static final ReadWriteLock byNameLock = new ReentrantReadWriteLock();
    public static final XStream2 XSTREAM = new XStream2();
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    static {
        XSTREAM.alias("user",User.class);
    }
    public ACL getACL() {
        final ACL base = Jenkins.getInstance().getAuthorizationStrategy().getACL(this);
        return new ACL() {
            public boolean hasPermission(Authentication a, Permission permission) {
                return (idStrategy().equals(a.getName(), id) && !(a instanceof AnonymousAuthenticationToken))
                        || base.hasPermission(a, permission);
            }
        };
    }
    public void checkPermission(Permission permission) {
        getACL().checkPermission(permission);
    }
    public boolean hasPermission(Permission permission) {
        return getACL().hasPermission(permission);
    }
    public boolean canDelete() {
        final IdStrategy strategy = idStrategy();
        return hasPermission(Jenkins.ADMINISTER) && !strategy.equals(id, Jenkins.getAuthentication().getName())
                && new File(getRootDir(), strategy.filenameOf(id)).exists();
    }
    public @Nonnull List<String> getAuthorities() {
        if (!Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER)) {
            return Collections.emptyList();
        }
        List<String> r = new ArrayList<String>();
        Authentication authentication;
        try {
            authentication = impersonate();
        } catch (UsernameNotFoundException x) {
            LOGGER.log(Level.FINE, "cannot look up authorities for " + id, x);
            return Collections.emptyList();
        }
        for (GrantedAuthority a : authentication.getAuthorities()) {
            if (a.equals(SecurityRealm.AUTHENTICATED_AUTHORITY)) {
                continue;
            }
            String n = a.getAuthority();
            if (n != null && !idStrategy().equals(n, id)) {
                r.add(n);
            }
        }
        Collections.sort(r, String.CASE_INSENSITIVE_ORDER);
        return r;
    }
    public Descriptor getDescriptorByName(String className) {
        return Jenkins.getInstance().getDescriptorByName(className);
    }
    public Object getDynamic(String token) {
        for(Action action: getTransientActions()){
            if(action.getUrlName().equals(token))
                return action;
        }
        for(Action action: getPropertyActions()){
            if(action.getUrlName().equals(token))
                return action;
        }
        return null;
    }
    public List<Action> getPropertyActions() {
        List<Action> actions = new ArrayList<Action>();
        for (UserProperty userProp : getProperties().values()) {
            if (userProp instanceof Action) {
                actions.add((Action) userProp);
            }
        }
        return Collections.unmodifiableList(actions);
    }
    public List<Action> getTransientActions() {
        List<Action> actions = new ArrayList<Action>();
        for (TransientUserActionFactory factory: TransientUserActionFactory.all()) {
            actions.addAll(factory.createFor(this));
        }
        return Collections.unmodifiableList(actions);
    }
    public ContextMenu doContextMenu(StaplerRequest request, StaplerResponse response) throws Exception {
        return new ContextMenu().from(this,request,response);
    }
    public static abstract class CanonicalIdResolver extends AbstractDescribableImpl<CanonicalIdResolver> implements ExtensionPoint, Comparable<CanonicalIdResolver> {
        public static final String REALM = "realm";
        public int compareTo(CanonicalIdResolver o) {
            int i = getPriority();
            int j = o.getPriority();
            return i>j ? -1 : (i==j ? 0:1);
        }
        public abstract @CheckForNull String resolveCanonicalId(String idOrFullName, Map<String, ?> context);
        public int getPriority() {
            return 1;
        }
    }
    @Extension
    public static class FullNameIdResolver extends CanonicalIdResolver {
        @Override
        public String resolveCanonicalId(String idOrFullName, Map<String, ?> context) {
            for (User user : getAll()) {
                if (idOrFullName.equals(user.getFullName())) return user.getId();
            }
            return null;
        }
        @Override
        public int getPriority() {
            return -1; 
        }
    }
    @Extension
    @Restricted(NoExternalUse.class)
    public static class UserIDCanonicalIdResolver extends User.CanonicalIdResolver {
        private static  boolean SECURITY_243_FULL_DEFENSE = Boolean.parseBoolean(System.getProperty(User.class.getName() + ".SECURITY_243_FULL_DEFENSE", "true"));
        private static final ThreadLocal<Boolean> resolving = new ThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() {
                return false;
            }
        };
        @Override
        public String resolveCanonicalId(String idOrFullName, Map<String, ?> context) {
            User existing = getById(idOrFullName, false);
            if (existing != null) {
                return existing.getId();
            }
            if (SECURITY_243_FULL_DEFENSE) {
                Jenkins j = Jenkins.getInstance();
                if (j != null) {
                    if (!resolving.get()) {
                        resolving.set(true);
                        try {
                            return j.getSecurityRealm().loadUserByUsername(idOrFullName).getUsername();
                        } catch (UsernameNotFoundException x) {
                            LOGGER.log(Level.FINE, "not sure whether " + idOrFullName + " is a valid username or not", x);
                        } catch (DataAccessException x) {
                            LOGGER.log(Level.FINE, "could not look up " + idOrFullName, x);
                        } finally {
                            resolving.set(false);
                        }
                    }
                }
            }
            return null;
        }
        @Override
        public int getPriority() {
            return Integer.MAX_VALUE;
        }
    }
    public static boolean ALLOW_NON_EXISTENT_USER_TO_LOGIN = Boolean.getBoolean(User.class.getName()+".allowNonExistentUserToLogin");
}
