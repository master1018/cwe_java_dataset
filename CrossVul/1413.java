
package hudson.model;
import hudson.Util;
import hudson.XmlFile;
import hudson.model.listeners.ItemListener;
import hudson.security.AccessControlled;
import hudson.util.CopyOnWriteMap;
import hudson.util.Function1;
import hudson.util.Secret;
import jenkins.model.Jenkins;
import jenkins.util.xml.XMLUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import jenkins.security.NotReallyRoleSensitiveCallable;
import org.acegisecurity.AccessDeniedException;
import org.xml.sax.SAXException;
public abstract class ItemGroupMixIn {
    private final ItemGroup parent;
    private final AccessControlled acl;
    protected ItemGroupMixIn(ItemGroup parent, AccessControlled acl) {
        this.parent = parent;
        this.acl = acl;
    }
    protected abstract void add(TopLevelItem item);
    protected abstract File getRootDirFor(String name);
    public static <K,V extends Item> Map<K,V> loadChildren(ItemGroup parent, File modulesDir, Function1<? extends K,? super V> key) {
        modulesDir.mkdirs(); 
        File[] subdirs = modulesDir.listFiles(new FileFilter() {
            public boolean accept(File child) {
                return child.isDirectory();
            }
        });
        CopyOnWriteMap.Tree<K,V> configurations = new CopyOnWriteMap.Tree<K,V>();
        for (File subdir : subdirs) {
            try {
                V item = (V) parent.getItem(subdir.getName());
                if (item == null) {
                    XmlFile xmlFile = Items.getConfigFile(subdir);
                    if (xmlFile.exists()) {
                        item = (V) Items.load(parent, subdir);
                    } else {
                        Logger.getLogger(ItemGroupMixIn.class.getName()).log(Level.WARNING, "could not find file " + xmlFile.getFile());
                        continue;
                    }
                } else {
                    item.onLoad(parent, subdir.getName());
                }
                configurations.put(key.call(item), item);
            } catch (Exception e) {
                Logger.getLogger(ItemGroupMixIn.class.getName()).log(Level.WARNING, "could not load " + subdir, e);
            }
        }
        return configurations;
    }
    public static final Function1<String,Item> KEYED_BY_NAME = new Function1<String, Item>() {
        public String call(Item item) {
            return item.getName();
        }
    };
    public synchronized TopLevelItem createTopLevelItem( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
        acl.checkPermission(Item.CREATE);
        TopLevelItem result;
        String requestContentType = req.getContentType();
        if(requestContentType==null)
            throw new Failure("No Content-Type header set");
        boolean isXmlSubmission = requestContentType.startsWith("application/xml") || requestContentType.startsWith("text/xml");
        String name = req.getParameter("name");
        if(name==null)
            throw new Failure("Query parameter 'name' is required");
        {
            Jenkins.checkGoodName(name);
            name = name.trim();
            if(parent.getItem(name)!=null)
                throw new Failure(Messages.Hudson_JobAlreadyExists(name));
        }
        String mode = req.getParameter("mode");
        if(mode!=null && mode.equals("copy")) {
            String from = req.getParameter("from");
            Item src = null;
            if (!from.startsWith("/"))
                src = parent.getItem(from);
            if (src==null)
                src = Jenkins.getInstance().getItemByFullName(from);
            if(src==null) {
                if(Util.fixEmpty(from)==null)
                    throw new Failure("Specify which job to copy");
                else
                    throw new Failure("No such job: "+from);
            }
            if (!(src instanceof TopLevelItem))
                throw new Failure(from+" cannot be copied");
            result = copy((TopLevelItem) src,name);
        } else {
            if(isXmlSubmission) {
                result = createProjectFromXML(name, req.getInputStream());
                rsp.setStatus(HttpServletResponse.SC_OK);
                return result;
            } else {
                if(mode==null)
                    throw new Failure("No mode given");
                TopLevelItemDescriptor descriptor = Items.all().findByName(mode);
                if (descriptor == null) {
                    throw new Failure("No item type ‘" + mode + "’ is known");
                }
                descriptor.checkApplicableIn(parent);
                acl.getACL().checkCreatePermission(parent, descriptor);
                result = createProject(descriptor, name, true);
            }
        }
        rsp.sendRedirect2(redirectAfterCreateItem(req, result));
        return result;
    }
    protected String redirectAfterCreateItem(StaplerRequest req, TopLevelItem result) throws IOException {
        return req.getContextPath()+'/'+result.getUrl()+"configure";
    }
    @SuppressWarnings({"unchecked"})
    public synchronized <T extends TopLevelItem> T copy(T src, String name) throws IOException {
        acl.checkPermission(Item.CREATE);
        src.checkPermission(Item.EXTENDED_READ);
        XmlFile srcConfigFile = Items.getConfigFile(src);
        if (!src.hasPermission(Item.CONFIGURE)) {
            Matcher matcher = AbstractItem.SECRET_PATTERN.matcher(srcConfigFile.asString());
            while (matcher.find()) {
                if (Secret.decrypt(matcher.group(1)) != null) {
                    throw new AccessDeniedException(Messages.ItemGroupMixIn_may_not_copy_as_it_contains_secrets_and_(src.getFullName(), Jenkins.getAuthentication().getName(), Item.PERMISSIONS.title, Item.EXTENDED_READ.name, Item.CONFIGURE.name));
                }
            }
        }
        src.getDescriptor().checkApplicableIn(parent);
        acl.getACL().checkCreatePermission(parent, src.getDescriptor());
        T result = (T)createProject(src.getDescriptor(),name,false);
        Util.copyFile(srcConfigFile.getFile(), Items.getConfigFile(result).getFile());
        final File rootDir = result.getRootDir();
        result = Items.whileUpdatingByXml(new NotReallyRoleSensitiveCallable<T,IOException>() {
            @Override public T call() throws IOException {
                return (T) Items.load(parent, rootDir);
            }
        });
        result.onCopiedFrom(src);
        add(result);
        ItemListener.fireOnCopied(src,result);
        Jenkins.getInstance().rebuildDependencyGraphAsync();
        return result;
    }
    public synchronized TopLevelItem createProjectFromXML(String name, InputStream xml) throws IOException {
        acl.checkPermission(Item.CREATE);
        Jenkins.getInstance().getProjectNamingStrategy().checkName(name);
        if (parent.getItem(name) != null) {
            throw new IllegalArgumentException(parent.getDisplayName() + " already contains an item '" + name + "'");
        }
        File configXml = Items.getConfigFile(getRootDirFor(name)).getFile();
        final File dir = configXml.getParentFile();
        dir.mkdirs();
        boolean success = false;
        try {
            XMLUtils.safeTransform((Source)new StreamSource(xml), new StreamResult(configXml));
            TopLevelItem result = Items.whileUpdatingByXml(new NotReallyRoleSensitiveCallable<TopLevelItem,IOException>() {
                @Override public TopLevelItem call() throws IOException {
                    return (TopLevelItem) Items.load(parent, dir);
                }
            });
            success = acl.getACL().hasCreatePermission(Jenkins.getAuthentication(), parent, result.getDescriptor())
                && result.getDescriptor().isApplicableIn(parent);
            add(result);
            ItemListener.fireOnCreated(result);
            Jenkins.getInstance().rebuildDependencyGraphAsync();
            return result;
        } catch (TransformerException e) {
            success = false;
            throw new IOException("Failed to persist config.xml", e);
        } catch (SAXException e) {
            success = false;
            throw new IOException("Failed to persist config.xml", e);
        } catch (IOException e) {
            success = false;
            throw e;
        } catch (RuntimeException e) {
            success = false;
            throw e;
        } finally {
            if (!success) {
                Util.deleteRecursive(dir);
            }
        }
    }
    public synchronized TopLevelItem createProject( TopLevelItemDescriptor type, String name, boolean notify )
            throws IOException {
        acl.checkPermission(Item.CREATE);
        type.checkApplicableIn(parent);
        acl.getACL().checkCreatePermission(parent, type);
        Jenkins.getInstance().getProjectNamingStrategy().checkName(name);
        if(parent.getItem(name)!=null)
            throw new IllegalArgumentException("Project of the name "+name+" already exists");
        TopLevelItem item = type.newInstance(parent, name);
        try {
            callOnCreatedFromScratch(item);
        } catch (AbstractMethodError e) {
        }
        item.save();
        add(item);
        Jenkins.getInstance().rebuildDependencyGraphAsync();
        if (notify)
            ItemListener.fireOnCreated(item);
        return item;
    }
    private void callOnCreatedFromScratch(TopLevelItem item) {
        item.onCreatedFromScratch();
    }
}
