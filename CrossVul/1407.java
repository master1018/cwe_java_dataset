
package hudson.model;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.XppDriver;
import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.Functions;
import hudson.Indenter;
import hudson.Util;
import hudson.model.Descriptor.FormException;
import hudson.model.labels.LabelAtomPropertyDescriptor;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import hudson.search.CollectionSearchIndex;
import hudson.search.SearchIndexBuilder;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.PermissionScope;
import hudson.tasks.UserAvatarResolver;
import hudson.util.AlternativeUiTextProvider;
import hudson.util.AlternativeUiTextProvider.Message;
import hudson.util.DescribableList;
import hudson.util.DescriptorList;
import hudson.util.FormApply;
import hudson.util.IOException2;
import hudson.util.RunList;
import hudson.util.XStream2;
import hudson.views.ListViewColumn;
import hudson.widgets.Widget;
import jenkins.model.Jenkins;
import jenkins.util.ProgressiveRendering;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.interceptor.RequirePOST;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jenkins.model.Jenkins.*;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
@ExportedBean
public abstract class View extends AbstractModelObject implements AccessControlled, Describable<View>, ExtensionPoint, Saveable {
    protected  ViewGroup owner;
    protected String name;
    protected String description;
    protected boolean filterExecutors;
    protected boolean filterQueue;
    protected transient List<Action> transientActions;
    private volatile DescribableList<ViewProperty,ViewPropertyDescriptor> properties = new PropertyList(this);
    protected View(String name) {
        this.name = name;
    }
    protected View(String name, ViewGroup owner) {
        this.name = name;
        this.owner = owner;
    }
    @Exported(name="jobs")
    public abstract Collection<TopLevelItem> getItems();
    public TopLevelItem getItem(String name) {
        return getOwnerItemGroup().getItem(name);
    }
    public final TopLevelItem getJob(String name) {
        return getItem(name);
    }
    public abstract boolean contains(TopLevelItem item);
    @Exported(visibility=2,name="name")
    public String getViewName() {
        return name;
    }
    public void rename(String newName) throws Failure, FormException {
        if(name.equals(newName))    return; 
        checkGoodName(newName);
        if(owner.getView(newName)!=null)
            throw new FormException(Messages.Hudson_ViewAlreadyExists(newName),"name");
        String oldName = name;
        name = newName;
        owner.onViewRenamed(this,oldName,newName);
    }
    public ViewGroup getOwner() {
        return owner;
    }
    public ItemGroup<? extends TopLevelItem> getOwnerItemGroup() {
        try {
            return _getOwnerItemGroup();
        } catch (AbstractMethodError e) {
            return Hudson.getInstance();
        }
    }
    private ItemGroup<? extends TopLevelItem> _getOwnerItemGroup() {
        return owner.getItemGroup();
    }
    public View getOwnerPrimaryView() {
        try {
            return _getOwnerPrimaryView();
        } catch (AbstractMethodError e) {
            return null;
        }
    }
    private View _getOwnerPrimaryView() {
        return owner.getPrimaryView();
    }
    public List<Action> getOwnerViewActions() {
        try {
            return _getOwnerViewActions();
        } catch (AbstractMethodError e) {
            return Hudson.getInstance().getActions();
        }
    }
    private List<Action> _getOwnerViewActions() {
        return owner.getViewActions();
    }
    @Exported
    public String getDescription() {
        return description;
    }
    public DescribableList<ViewProperty,ViewPropertyDescriptor> getProperties() {
        synchronized (PropertyList.class) {
            if (properties == null) {
                properties = new PropertyList(this);
            } else {
                properties.setOwner(this);
            }
            return properties;
        }
    }
    public List<ViewPropertyDescriptor> getApplicablePropertyDescriptors() {
        List<ViewPropertyDescriptor> r = new ArrayList<ViewPropertyDescriptor>();
        for (ViewPropertyDescriptor pd : ViewProperty.all()) {
            if (pd.isEnabledFor(this))
                r.add(pd);
        }
        return r;
    }
    public void save() throws IOException {
        if (owner != null) {
            owner.save();
        }
    }
    @Exported(name="property",inline=true)
    public List<ViewProperty> getAllProperties() {
        return getProperties().toList();
    }
    public ViewDescriptor getDescriptor() {
        return (ViewDescriptor) Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
    public String getDisplayName() {
        return getViewName();
    }
    public String getNewPronoun() {
        return AlternativeUiTextProvider.get(NEW_PRONOUN, this, Messages.AbstractItem_Pronoun());
    }
    public boolean isEditable() {
        return true;
    }
    public boolean isFilterExecutors() {
        return filterExecutors;
    }
    public boolean isFilterQueue() {
        return filterQueue;
    }
    public List<Widget> getWidgets() {
        return Collections.unmodifiableList(Jenkins.getInstance().getWidgets());
    }
    public Iterable<? extends ListViewColumn> getColumns() {
        return ListViewColumn.createDefaultInitialColumnList();
    }
    public Indenter getIndenter() {
        return null;
    }
    public boolean isDefault() {
        return getOwnerPrimaryView()==this;
    }
    public List<Computer> getComputers() {
        Computer[] computers = Jenkins.getInstance().getComputers();
        if (!isFilterExecutors()) {
            return Arrays.asList(computers);
        }
        List<Computer> result = new ArrayList<Computer>();
        HashSet<Label> labels = new HashSet<Label>();
        for (Item item : getItems()) {
            if (item instanceof AbstractProject<?, ?>) {
                labels.addAll(((AbstractProject<?, ?>) item).getRelevantLabels());
            }
        }
        for (Computer c : computers) {
            Node n = c.getNode();
            if (n != null) {
                if (labels.contains(null) && n.getMode() == Mode.NORMAL || !isDisjoint(n.getAssignedLabels(), labels)) {
                    result.add(c);
                }
            }
        }
        return result;
    }
    private boolean isDisjoint(Collection c1, Collection c2) {
        for (Object o : c1)
            if (c2.contains(o))
                return false;
        return true;
    }
    private List<Queue.Item> filterQueue(List<Queue.Item> base) {
        if (!isFilterQueue()) {
            return base;
        }
        Collection<TopLevelItem> items = getItems();
        List<Queue.Item> result = new ArrayList<Queue.Item>();
        for (Queue.Item qi : base) {
            if (items.contains(qi.task)) {
                result.add(qi);
            } else
            if (qi.task instanceof AbstractProject<?, ?>) {
                AbstractProject<?,?> project = (AbstractProject<?, ?>) qi.task;
                if (items.contains(project.getRootProject())) {
                    result.add(qi);
                }
            }
        }
        return result;
    }
    public List<Queue.Item> getQueueItems() {
        return filterQueue(Arrays.asList(Jenkins.getInstance().getQueue().getItems()));
    }
    public List<Queue.Item> getApproximateQueueItemsQuickly() {
        return filterQueue(Jenkins.getInstance().getQueue().getApproximateItemsQuickly());
    }
    public String getUrl() {
        return isDefault() ? (owner!=null ? owner.getUrl() : "") : getViewUrl();
    }
    public String getViewUrl() {
        return (owner!=null ? owner.getUrl() : "") + "view/" + Util.rawEncode(getViewName()) + '/';
    }
    public String getSearchUrl() {
        return getUrl();
    }
    public List<Action> getActions() {
    	List<Action> result = new ArrayList<Action>();
    	result.addAll(getOwnerViewActions());
    	synchronized (this) {
    		if (transientActions == null) {
                updateTransientActions();
    		}
    		result.addAll(transientActions);
    	}
    	return result;
    }
    public synchronized void updateTransientActions() {
        transientActions = TransientViewActionFactory.createAllFor(this); 
    }
    public Object getDynamic(String token) {
        for (Action a : getActions()) {
            String url = a.getUrlName();
            if (url==null)  continue;
            if(a.getUrlName().equals(token))
                return a;
        }
        return null;
    }
    @Exported(visibility=2,name="url")
    public String getAbsoluteUrl() {
        return Jenkins.getInstance().getRootUrl()+getUrl();
    }
    public Api getApi() {
        return new Api(this);
    }
    public String getPostConstructLandingPage() {
        return "configure";
    }
    public ACL getACL() {
        return Jenkins.getInstance().getAuthorizationStrategy().getACL(this);
    }
    public void checkPermission(Permission p) {
        getACL().checkPermission(p);
    }
    public boolean hasPermission(Permission p) {
        return getACL().hasPermission(p);
    }
    public abstract void onJobRenamed(Item item, String oldName, String newName);
    @ExportedBean(defaultVisibility=2)
    public static final class UserInfo implements Comparable<UserInfo> {
        private final User user;
        private Calendar lastChange;
        private AbstractProject project;
        String avatar;
        UserInfo(User user, AbstractProject p, Calendar lastChange) {
            this.user = user;
            this.project = p;
            this.lastChange = lastChange;
        }
        @Exported
        public User getUser() {
            return user;
        }
        @Exported
        public Calendar getLastChange() {
            return lastChange;
        }
        @Exported
        public AbstractProject getProject() {
            return project;
        }
        public String getLastChangeTimeString() {
            if(lastChange==null)    return "N/A";
            long duration = new GregorianCalendar().getTimeInMillis()- ordinal();
            return Util.getTimeSpanString(duration);
        }
        public String getTimeSortKey() {
            if(lastChange==null)    return "-";
            return Util.XS_DATETIME_FORMATTER.format(lastChange.getTime());
        }
        public int compareTo(UserInfo that) {
            long rhs = that.ordinal();
            long lhs = this.ordinal();
            if(rhs>lhs) return 1;
            if(rhs<lhs) return -1;
            return 0;
        }
        private long ordinal() {
            if(lastChange==null)    return 0;
            return lastChange.getTimeInMillis();
        }
    }
    public boolean hasPeople() {
        return People.isApplicable(getItems());
    }
    public People getPeople() {
        return new People(this);
    }
    public AsynchPeople getAsynchPeople() {
        return new AsynchPeople(this);
    }
    @ExportedBean
    public static final class People  {
        @Exported
        public final List<UserInfo> users;
        public final ModelObject parent;
        public People(Jenkins parent) {
            this.parent = parent;
            Map<User,UserInfo> users = getUserInfo(parent.getItems());
            User unknown = User.getUnknown();
            for (User u : User.getAll()) {
                if(u==unknown)  continue;   
                if(!users.containsKey(u))
                    users.put(u,new UserInfo(u,null,null));
            }
            this.users = toList(users);
        }
        public People(View parent) {
            this.parent = parent;
            this.users = toList(getUserInfo(parent.getItems()));
        }
        private Map<User,UserInfo> getUserInfo(Collection<? extends Item> items) {
            Map<User,UserInfo> users = new HashMap<User,UserInfo>();
            for (Item item : items) {
                for (Job job : item.getAllJobs()) {
                    if (job instanceof AbstractProject) {
                        AbstractProject<?,?> p = (AbstractProject) job;
                        for (AbstractBuild<?,?> build : p.getBuilds()) {
                            for (Entry entry : build.getChangeSet()) {
                                User user = entry.getAuthor();
                                UserInfo info = users.get(user);
                                if(info==null)
                                    users.put(user,new UserInfo(user,p,build.getTimestamp()));
                                else
                                if(info.getLastChange().before(build.getTimestamp())) {
                                    info.project = p;
                                    info.lastChange = build.getTimestamp();
                                }
                            }
                        }
                    }
                }
            }
            return users;
        }
        private List<UserInfo> toList(Map<User,UserInfo> users) {
            ArrayList<UserInfo> list = new ArrayList<UserInfo>();
            list.addAll(users.values());
            Collections.sort(list);
            return Collections.unmodifiableList(list);
        }
        public Api getApi() {
            return new Api(this);
        }
        public static boolean isApplicable(Collection<? extends Item> items) {
            for (Item item : items) {
                for (Job job : item.getAllJobs()) {
                    if (job instanceof AbstractProject) {
                        AbstractProject<?,?> p = (AbstractProject) job;
                        for (AbstractBuild<?,?> build : p.getBuilds()) {
                            for (Entry entry : build.getChangeSet()) {
                                User user = entry.getAuthor();
                                if(user!=null)
                                    return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
    public static final class AsynchPeople extends ProgressiveRendering { 
        private final Collection<TopLevelItem> items;
        private final User unknown;
        private final Map<User,UserInfo> users = new HashMap<User,UserInfo>();
        private final Set<User> modified = new HashSet<User>();
        private final String iconSize;
        public final ModelObject parent;
        public AsynchPeople(Jenkins parent) {
            this.parent = parent;
            items = parent.getItems();
            unknown = User.getUnknown();
        }
        public AsynchPeople(View parent) {
            this.parent = parent;
            items = parent.getItems();
            unknown = null;
        }
        {
            StaplerRequest req = Stapler.getCurrentRequest();
            iconSize = req != null ? Functions.getCookie(req, "iconSize", "32x32") : "32x32";
        }
        @Override protected void compute() throws Exception {
            int itemCount = 0;
            for (Item item : items) {
                for (Job<?,?> job : item.getAllJobs()) {
                    if (job instanceof AbstractProject) {
                        AbstractProject<?,?> p = (AbstractProject) job;
                        RunList<? extends AbstractBuild<?,?>> builds = p.getBuilds();
                        int buildCount = 0;
                        for (AbstractBuild<?,?> build : builds) {
                            if (canceled()) {
                                return;
                            }
                            for (ChangeLogSet.Entry entry : build.getChangeSet()) {
                                User user = entry.getAuthor();
                                UserInfo info = users.get(user);
                                if (info == null) {
                                    UserInfo userInfo = new UserInfo(user, p, build.getTimestamp());
                                    userInfo.avatar = UserAvatarResolver.resolve(user, iconSize);
                                    synchronized (this) {
                                        users.put(user, userInfo);
                                        modified.add(user);
                                    }
                                } else if (info.getLastChange().before(build.getTimestamp())) {
                                    synchronized (this) {
                                        info.project = p;
                                        info.lastChange = build.getTimestamp();
                                        modified.add(user);
                                    }
                                }
                            }
                            buildCount++;
                            progress((itemCount + 1.0 * buildCount / builds.size()) / (items.size() + 1));
                        }
                    }
                }
                itemCount++;
                progress(1.0 * itemCount / (items.size() + 1));
            }
            if (unknown != null) {
                if (canceled()) {
                    return;
                }
                for (User u : User.getAll()) { 
                    if (u == unknown) {
                        continue;
                    }
                    if (!users.containsKey(u)) {
                        UserInfo userInfo = new UserInfo(u, null, null);
                        userInfo.avatar = UserAvatarResolver.resolve(u, iconSize);
                        synchronized (this) {
                            users.put(u, userInfo);
                            modified.add(u);
                        }
                    }
                }
            }
        }
        @Override protected synchronized JSON data() {
            JSONArray r = new JSONArray();
            for (User u : modified) {
                UserInfo i = users.get(u);
                JSONObject entry = new JSONObject().
                        accumulate("id", u.getId()).
                        accumulate("fullName", u.getFullName()).
                        accumulate("url", u.getUrl()).
                        accumulate("avatar", i.avatar).
                        accumulate("timeSortKey", i.getTimeSortKey()).
                        accumulate("lastChangeTimeString", i.getLastChangeTimeString());
                AbstractProject<?,?> p = i.getProject();
                if (p != null) {
                    entry.accumulate("projectUrl", p.getUrl()).accumulate("projectFullDisplayName", p.getFullDisplayName());
                }
                r.add(entry);
            }
            modified.clear();
            return r;
        }
        public Api getApi() {
            return new Api(new People());
        }
        @Restricted(NoExternalUse.class)
        @ExportedBean
        public final class People {
            private View.People people;
            @Exported public synchronized List<UserInfo> getUsers() {
                if (people == null) {
                    people = parent instanceof Jenkins ? new View.People((Jenkins) parent) : new View.People((View) parent);
                }
                return people.users;
            }
        }
    }
    void addDisplayNamesToSearchIndex(SearchIndexBuilder sib, Collection<TopLevelItem> items) {
        for(TopLevelItem item : items) {
            if(LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine((String.format("Adding url=%s,displayName=%s",
                            item.getSearchUrl(), item.getDisplayName())));
            }
            sib.add(item.getSearchUrl(), item.getDisplayName());
        }        
    }
    @Override
    public SearchIndexBuilder makeSearchIndex() {
        SearchIndexBuilder sib = super.makeSearchIndex();
        sib.add(new CollectionSearchIndex<TopLevelItem>() {
                protected TopLevelItem get(String key) { return getItem(key); }
                protected Collection<TopLevelItem> all() { return getItems(); }                
                @Override
                protected String getName(TopLevelItem o) {
                    return o.getName();
                }
            });
        addDisplayNamesToSearchIndex(sib, getItems());
        return sib;
    }
    public synchronized void doSubmitDescription( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        checkPermission(CONFIGURE);
        description = req.getParameter("description");
        save();
        rsp.sendRedirect(".");  
    }
    @RequirePOST
    public final synchronized void doConfigSubmit( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException, FormException {
        checkPermission(CONFIGURE);
        submit(req);
        description = Util.nullify(req.getParameter("description"));
        filterExecutors = req.getParameter("filterExecutors") != null;
        filterQueue = req.getParameter("filterQueue") != null;
        rename(req.getParameter("name"));
        getProperties().rebuild(req, req.getSubmittedForm(), getApplicablePropertyDescriptors());
        updateTransientActions();  
        save();
        FormApply.success("../"+name).generateResponse(req,rsp,this);
    }
    protected abstract void submit(StaplerRequest req) throws IOException, ServletException, FormException;
    @RequirePOST
    public synchronized void doDoDelete(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        checkPermission(DELETE);
        owner.deleteView(this);
        rsp.sendRedirect2(req.getContextPath()+"/" + owner.getUrl());
    }
    public abstract Item doCreateItem( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException;
    public void doRssAll( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        rss(req, rsp, " all builds", getBuilds());
    }
    public void doRssFailed( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        rss(req, rsp, " failed builds", getBuilds().failureOnly());
    }
    public RunList getBuilds() {
        return new RunList(this);
    }
    public BuildTimelineWidget getTimeline() {
        return new BuildTimelineWidget(getBuilds());
    }
    private void rss(StaplerRequest req, StaplerResponse rsp, String suffix, RunList runs) throws IOException, ServletException {
        RSS.forwardToRss(getDisplayName()+ suffix, getUrl(),
            runs.newBuilds(), Run.FEED_ADAPTER, req, rsp );
    }
    public void doRssLatest( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        List<Run> lastBuilds = new ArrayList<Run>();
        for (TopLevelItem item : getItems()) {
            if (item instanceof Job) {
                Job job = (Job) item;
                Run lb = job.getLastBuild();
                if(lb!=null)    lastBuilds.add(lb);
            }
        }
        RSS.forwardToRss(getDisplayName()+" last builds only", getUrl(),
            lastBuilds, Run.FEED_ADAPTER_LATEST, req, rsp );
    }
    @WebMethod(name = "config.xml")
    public HttpResponse doConfigDotXml(StaplerRequest req) throws IOException {
        if (req.getMethod().equals("GET")) {
            checkPermission(READ);
            return new HttpResponse() {
                public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
                    rsp.setContentType("application/xml");
                    XStream2 xStream2 = new XStream2();
                    xStream2.omitField(View.class, "owner");
                    xStream2.toXMLUTF8(this,  rsp.getOutputStream());
                }
            };
        }
        if (req.getMethod().equals("POST")) {
            updateByXml((Source)new StreamSource(req.getReader()));
            return HttpResponses.ok();
        }
        return HttpResponses.error(SC_BAD_REQUEST, "Unexpected request method " + req.getMethod());
    }
    public void updateByXml(Source source) throws IOException {
        checkPermission(CONFIGURE);
        StringWriter out = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance()
                    .newTransformer();
            t.transform(source,
                    new StreamResult(out));
            out.close();
        } catch (TransformerException e) {
            throw new IOException2("Failed to persist configuration.xml", e);
        }
        InputStream in = new BufferedInputStream(new ByteArrayInputStream(out.toString().getBytes("UTF-8")));
        try {
            Jenkins.XSTREAM.unmarshal(new XppDriver().createReader(in), this);
        } catch (StreamException e) {
            throw new IOException2("Unable to read",e);
        } catch(ConversionException e) {
            throw new IOException2("Unable to read",e);
        } catch(Error e) {
            throw new IOException2("Unable to read",e);
        } finally {
            in.close();
        }
    }
    public static final DescriptorList<View> LIST = new DescriptorList<View>(View.class);
    public static DescriptorExtensionList<View,ViewDescriptor> all() {
        return Jenkins.getInstance().<View,ViewDescriptor>getDescriptorList(View.class);
    }
    public static List<ViewDescriptor> allInstantiable() {
        List<ViewDescriptor> r = new ArrayList<ViewDescriptor>();
        for (ViewDescriptor d : all())
            if(d.isInstantiable())
                r.add(d);
        return r;
    }
    public static final Comparator<View> SORTER = new Comparator<View>() {
        public int compare(View lhs, View rhs) {
            return lhs.getViewName().compareTo(rhs.getViewName());
        }
    };
    public static final PermissionGroup PERMISSIONS = new PermissionGroup(View.class,Messages._View_Permissions_Title());
    public static final Permission CREATE = new Permission(PERMISSIONS,"Create", Messages._View_CreatePermission_Description(), Permission.CREATE, PermissionScope.ITEM_GROUP);
    public static final Permission DELETE = new Permission(PERMISSIONS,"Delete", Messages._View_DeletePermission_Description(), Permission.DELETE, PermissionScope.ITEM_GROUP);
    public static final Permission CONFIGURE = new Permission(PERMISSIONS,"Configure", Messages._View_ConfigurePermission_Description(), Permission.CONFIGURE, PermissionScope.ITEM_GROUP);
    public static final Permission READ = new Permission(PERMISSIONS,"Read", Messages._View_ReadPermission_Description(), Permission.READ, PermissionScope.ITEM_GROUP);
    public static Permission getItemCreatePermission() {
        return Item.CREATE;
    }
    public static View create(StaplerRequest req, StaplerResponse rsp, ViewGroup owner)
            throws FormException, IOException, ServletException {
        String requestContentType = req.getContentType();
        if(requestContentType==null)
            throw new Failure("No Content-Type header set");
        boolean isXmlSubmission = requestContentType.startsWith("application/xml") || requestContentType.startsWith("text/xml");
        String name = req.getParameter("name");
        checkGoodName(name);
        if(owner.getView(name)!=null)
            throw new FormException(Messages.Hudson_ViewAlreadyExists(name),"name");
        String mode = req.getParameter("mode");
        if (mode==null || mode.length()==0) {
            if(isXmlSubmission) {
                View v;
                v = createViewFromXML(name, req.getInputStream());
                v.owner = owner;
                rsp.setStatus(HttpServletResponse.SC_OK);
                return v;
            } else
                throw new FormException(Messages.View_MissingMode(),"mode");
        }
        View v = all().findByName(mode).newInstance(req,req.getSubmittedForm());
        v.owner = owner;
        rsp.sendRedirect2(req.getContextPath()+'/'+v.getUrl()+v.getPostConstructLandingPage());
        return v;
    }
    public static View createViewFromXML(String name, InputStream xml) throws IOException {
        InputStream in = new BufferedInputStream(xml);
        try {
            View v = (View) Jenkins.XSTREAM.fromXML(in);
            v.name = name;
            return v;
        } catch(StreamException e) {
            throw new IOException2("Unable to read",e);
        } catch(ConversionException e) {
            throw new IOException2("Unable to read",e);
        } catch(Error e) {
            throw new IOException2("Unable to read",e);
        } finally {
            in.close();
        }
    }
    public static class PropertyList extends DescribableList<ViewProperty,ViewPropertyDescriptor> {
        private PropertyList(View owner) {
            super(owner);
        }
        public PropertyList() {
        }
        public View getOwner() {
            return (View)owner;
        }
        @Override
        protected void onModified() throws IOException {
            for (ViewProperty p : this)
                p.setView(getOwner());
        }
    }
    public static final Message<View> NEW_PRONOUN = new Message<View>();
    private final static Logger LOGGER = Logger.getLogger(View.class.getName());
}
