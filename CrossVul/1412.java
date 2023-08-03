
package hudson.model;
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import hudson.AbortException;
import hudson.XmlFile;
import hudson.Util;
import hudson.Functions;
import hudson.BulkChange;
import hudson.cli.declarative.CLIMethod;
import hudson.cli.declarative.CLIResolver;
import hudson.model.listeners.ItemListener;
import hudson.model.listeners.SaveableListener;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import hudson.security.ACL;
import hudson.util.AlternativeUiTextProvider;
import hudson.util.AlternativeUiTextProvider.Message;
import hudson.util.AtomicFileWriter;
import hudson.util.IOUtils;
import hudson.util.Secret;
import jenkins.model.DirectlyModifiableTopLevelItemGroup;
import jenkins.model.Jenkins;
import jenkins.security.NotReallyRoleSensitiveCallable;
import org.acegisecurity.Authentication;
import jenkins.util.xml.XMLUtils;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.HttpDeletable;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.stapler.interceptor.RequirePOST;
import org.xml.sax.SAXException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import org.apache.commons.io.FileUtils;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.Ancestor;
@ExportedBean
public abstract class AbstractItem extends Actionable implements Item, HttpDeletable, AccessControlled, DescriptorByNameOwner {
    private static final Logger LOGGER = Logger.getLogger(AbstractItem.class.getName());
    protected  transient String name;
    protected volatile String description;
    private transient ItemGroup parent;
    protected String displayName;
    protected AbstractItem(ItemGroup parent, String name) {
        this.parent = parent;
        doSetName(name);
    }
    public void onCreatedFromScratch() {
    }
    @Exported(visibility=999)
    public String getName() {
        return name;
    }
    public String getPronoun() {
        return AlternativeUiTextProvider.get(PRONOUN, this, Messages.AbstractItem_Pronoun());
    }
    @Exported
    public String getDisplayName() {
        if(null!=displayName) {
            return displayName;
        }
        return getName();
    }
    @Exported
    public String getDisplayNameOrNull() {
        return displayName;
    }
    public void setDisplayNameOrNull(String displayName) throws IOException {
        setDisplayName(displayName);
    }
    public void setDisplayName(String displayName) throws IOException {
        this.displayName = Util.fixEmpty(displayName);
        save();
    }
    public File getRootDir() {
        return getParent().getRootDirFor(this);
    }
    @WithBridgeMethods(value=Jenkins.class,castRequired=true)
    @Override public @Nonnull ItemGroup getParent() {
        if (parent == null) {
            throw new IllegalStateException("no parent set on " + getClass().getName() + "[" + name + "]");
        }
        return parent;
    }
    @Exported
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) throws IOException {
        this.description = description;
        save();
        ItemListener.fireOnUpdated(this);
    }
    protected void doSetName(String name) {
        this.name = name;
    }
    protected void renameTo(final String newName) throws IOException {
        final ItemGroup parent = getParent();
        String oldName = this.name;
        String oldFullName = getFullName();
        synchronized (parent) {
            synchronized (this) {
                if (newName == null)
                    throw new IllegalArgumentException("New name is not given");
                if (this.name.equals(newName))
                    return;
                ACL.impersonate(ACL.SYSTEM,new NotReallyRoleSensitiveCallable<Void,IOException>() {
                    final Authentication user = Jenkins.getAuthentication();
                    @Override
                    public Void call() throws IOException {
                        Item existing = parent.getItem(newName);
                        if (existing != null && existing!=AbstractItem.this) {
                            if (existing.getACL().hasPermission(user,Item.DISCOVER))
                                throw new IllegalArgumentException("Job " + newName + " already exists");
                            else {
                                throw new IOException("Unable to rename to " + newName);
                            }
                        }
                        return null;
                    }
                });
                File oldRoot = this.getRootDir();
                doSetName(newName);
                File newRoot = this.getRootDir();
                boolean success = false;
                try {
                    boolean interrupted = false;
                    boolean renamed = false;
                    for (int retry = 0; retry < 5; retry++) {
                        if (oldRoot.renameTo(newRoot)) {
                            renamed = true;
                            break; 
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            interrupted = true;
                        }
                    }
                    if (interrupted)
                        Thread.currentThread().interrupt();
                    if (!renamed) {
                        Copy cp = new Copy();
                        cp.setProject(new org.apache.tools.ant.Project());
                        cp.setTodir(newRoot);
                        FileSet src = new FileSet();
                        src.setDir(oldRoot);
                        cp.addFileset(src);
                        cp.setOverwrite(true);
                        cp.setPreserveLastModified(true);
                        cp.setFailOnError(false); 
                        cp.execute();
                        try {
                            Util.deleteRecursive(oldRoot);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    success = true;
                } finally {
                    if (!success)
                        doSetName(oldName);
                }
                try {
                    parent.onRenamed(this, oldName, newName);
                } catch (AbstractMethodError _) {
                }
            }
        }
        ItemListener.fireLocationChange(this, oldFullName);
    }
    public void movedTo(DirectlyModifiableTopLevelItemGroup destination, AbstractItem newItem, File destDir) throws IOException {
        newItem.onLoad(destination, name);
    }
    public abstract Collection<? extends Job> getAllJobs();
    public final String getFullName() {
        String n = getParent().getFullName();
        if(n.length()==0)   return getName();
        else                return n+'/'+getName();
    }
    public final String getFullDisplayName() {
        String n = getParent().getFullDisplayName();
        if(n.length()==0)   return getDisplayName();
        else                return n+" » "+getDisplayName();
    }
    public String getRelativeDisplayNameFrom(ItemGroup p) {
        return Functions.getRelativeDisplayNameFrom(this, p);
    }
    public String getRelativeNameFromGroup(ItemGroup p) {
        return getRelativeNameFrom(p);
    }
    public String getRelativeNameFrom(ItemGroup p) {
        return Functions.getRelativeNameFrom(this, p);
    }
    public String getRelativeNameFrom(Item item) {
        return getRelativeNameFrom(item.getParent());
    }
    public void onLoad(ItemGroup<? extends Item> parent, String name) throws IOException {
        this.parent = parent;
        doSetName(name);
    }
    public void onCopiedFrom(Item src) {
    }
    public final String getUrl() {
        StaplerRequest req = Stapler.getCurrentRequest();
        String shortUrl = getShortUrl();
        String uri = req == null ? null : req.getRequestURI();
        if (req != null) {
            String seed = Functions.getNearestAncestorUrl(req,this);
            LOGGER.log(Level.FINER, "seed={0} for {1} from {2}", new Object[] {seed, this, uri});
            if(seed!=null) {
                return seed.substring(req.getContextPath().length()+1)+'/';
            }
            List<Ancestor> ancestors = req.getAncestors();
            if (!ancestors.isEmpty()) {
                Ancestor last = ancestors.get(ancestors.size() - 1);
                if (last.getObject() instanceof View) {
                    View view = (View) last.getObject();
                    if (view.getOwnerItemGroup() == getParent() && !view.isDefault()) {
                        String base = last.getUrl().substring(req.getContextPath().length() + 1) + '/';
                        LOGGER.log(Level.FINER, "using {0}{1} for {2} from {3}", new Object[] {base, shortUrl, this, uri});
                        return base + shortUrl;
                    } else {
                        LOGGER.log(Level.FINER, "irrelevant {0} for {1} from {2}", new Object[] {view.getViewName(), this, uri});
                    }
                } else {
                    LOGGER.log(Level.FINER, "inapplicable {0} for {1} from {2}", new Object[] {last.getObject(), this, uri});
                }
            } else {
                LOGGER.log(Level.FINER, "no ancestors for {0} from {1}", new Object[] {this, uri});
            }
        } else {
            LOGGER.log(Level.FINER, "no current request for {0}", this);
        }
        String base = getParent().getUrl();
        LOGGER.log(Level.FINER, "falling back to {0}{1} for {2} from {3}", new Object[] {base, shortUrl, this, uri});
        return base + shortUrl;
    }
    public String getShortUrl() {
        String prefix = getParent().getUrlChildPrefix();
        String subdir = Util.rawEncode(getName());
        return prefix.equals(".") ? subdir + '/' : prefix + '/' + subdir + '/';
    }
    public String getSearchUrl() {
        return getShortUrl();
    }
    @Exported(visibility=999,name="url")
    public final String getAbsoluteUrl() {
        String r = Jenkins.getInstance().getRootUrl();
        if(r==null)
            throw new IllegalStateException("Root URL isn't configured yet. Cannot compute absolute URL.");
        return Util.encode(r+getUrl());
    }
    public final Api getApi() {
        return new Api(this);
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
    public synchronized void save() throws IOException {
        if(BulkChange.contains(this))   return;
        getConfigFile().write(this);
        SaveableListener.fireOnChange(this, getConfigFile());
    }
    public final XmlFile getConfigFile() {
        return Items.getConfigFile(this);
    }
    public Descriptor getDescriptorByName(String className) {
        return Jenkins.getInstance().getDescriptorByName(className);
    }
    public synchronized void doSubmitDescription( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        checkPermission(CONFIGURE);
        setDescription(req.getParameter("description"));
        rsp.sendRedirect(".");  
    }
    @RequirePOST
    public void doDoDelete( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException, InterruptedException {
        delete();
        if (req == null || rsp == null) { 
            return;
        }
        List<Ancestor> ancestors = req.getAncestors();
        ListIterator<Ancestor> it = ancestors.listIterator(ancestors.size());
        String url = getParent().getUrl(); 
        while (it.hasPrevious()) {
            Object a = it.previous().getObject();
            if (a instanceof View) {
                url = ((View) a).getUrl();
                break;
            } else if (a instanceof ViewGroup && a != this) {
                url = ((ViewGroup) a).getUrl();
                break;
            }
        }
        rsp.sendRedirect2(req.getContextPath() + '/' + url);
    }
    public void delete( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        try {
            doDoDelete(req,rsp);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }
    public void delete() throws IOException, InterruptedException {
        checkPermission(DELETE);
        synchronized (this) { 
            performDelete();
        } 
        getParent().onDeleted(AbstractItem.this);
        Jenkins.getInstance().rebuildDependencyGraphAsync();
    }
    protected void performDelete() throws IOException, InterruptedException {
        getConfigFile().delete();
        Util.deleteRecursive(getRootDir());
    }
    @WebMethod(name = "config.xml")
    public void doConfigDotXml(StaplerRequest req, StaplerResponse rsp)
            throws IOException {
        if (req.getMethod().equals("GET")) {
            rsp.setContentType("application/xml");
            writeConfigDotXml(rsp.getOutputStream());
            return;
        }
        if (req.getMethod().equals("POST")) {
            updateByXml((Source)new StreamSource(req.getReader()));
            return;
        }
        rsp.sendError(SC_BAD_REQUEST);
    }
    static final Pattern SECRET_PATTERN = Pattern.compile(">(" + Secret.ENCRYPTED_VALUE_PATTERN + ")<");
    @Restricted(NoExternalUse.class)
    public void writeConfigDotXml(OutputStream os) throws IOException {
        checkPermission(EXTENDED_READ);
        XmlFile configFile = getConfigFile();
        if (hasPermission(CONFIGURE)) {
            IOUtils.copy(configFile.getFile(), os);
        } else {
            String encoding = configFile.sniffEncoding();
            String xml = FileUtils.readFileToString(configFile.getFile(), encoding);
            Matcher matcher = SECRET_PATTERN.matcher(xml);
            StringBuffer cleanXml = new StringBuffer();
            while (matcher.find()) {
                if (Secret.decrypt(matcher.group(1)) != null) {
                    matcher.appendReplacement(cleanXml, ">********<");
                }
            }
            matcher.appendTail(cleanXml);
            org.apache.commons.io.IOUtils.write(cleanXml.toString(), os, encoding);
        }
    }
    @Deprecated
    public void updateByXml(StreamSource source) throws IOException {
        updateByXml((Source)source);
    }
    public void updateByXml(Source source) throws IOException {
        checkPermission(CONFIGURE);
        XmlFile configXmlFile = getConfigFile();
        final AtomicFileWriter out = new AtomicFileWriter(configXmlFile.getFile());
        try {
            try {
                XMLUtils.safeTransform(source, new StreamResult(out));
                out.close();
            } catch (TransformerException e) {
                throw new IOException("Failed to persist config.xml", e);
            } catch (SAXException e) {
                throw new IOException("Failed to persist config.xml", e);
            }
            Object o = new XmlFile(Items.XSTREAM, out.getTemporaryFile()).unmarshal(this);
            if (o!=this) {
                throw new IOException("Expecting "+this.getClass()+" but got "+o.getClass()+" instead");
            }
            Items.whileUpdatingByXml(new NotReallyRoleSensitiveCallable<Void,IOException>() {
                @Override public Void call() throws IOException {
                    onLoad(getParent(), getRootDir().getName());
                    return null;
                }
            });
            Jenkins.getInstance().rebuildDependencyGraphAsync();
            out.commit();
            SaveableListener.fireOnChange(this, getConfigFile());
        } finally {
            out.abort(); 
        }
    }
    @CLIMethod(name="reload-job")
    @RequirePOST
    public void doReload() throws IOException {
        checkPermission(CONFIGURE);
        getConfigFile().unmarshal(this);
        Items.whileUpdatingByXml(new NotReallyRoleSensitiveCallable<Void, IOException>() {
            @Override
            public Void call() throws IOException {
                onLoad(getParent(), getRootDir().getName());
                return null;
            }
        });
        Jenkins.getInstance().rebuildDependencyGraphAsync();
        SaveableListener.fireOnChange(this, getConfigFile());
    }
    @Override
    public String getSearchName() {
        return getName();
    }
    @Override public String toString() {
        return super.toString() + '[' + (parent != null ? getFullName() : "?/" + name) + ']';
    }
    @CLIResolver
    public static AbstractItem resolveForCLI(
            @Argument(required=true,metaVar="NAME",usage="Job name") String name) throws CmdLineException {
        AbstractItem item = Jenkins.getInstance().getItemByFullName(name, AbstractItem.class);
        if (item==null)
            throw new CmdLineException(null,Messages.AbstractItem_NoSuchJobExists(name,AbstractProject.findNearest(name).getFullName()));
        return item;
    }
    public static final Message<AbstractItem> PRONOUN = new Message<AbstractItem>();
}
