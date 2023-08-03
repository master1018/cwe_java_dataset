
package hudson.model;
import hudson.Functions;
import hudson.security.PermissionScope;
import org.kohsuke.stapler.StaplerRequest;
import java.io.IOException;
import java.util.Collection;
import hudson.search.SearchableModelObject;
import hudson.security.Permission;
import hudson.security.PermissionGroup;
import hudson.security.AccessControlled;
import hudson.util.Secret;
public interface Item extends PersistenceRoot, SearchableModelObject, AccessControlled {
    ItemGroup<? extends Item> getParent();
    Collection<? extends Job> getAllJobs();
    String getName();
    String getFullName();
    String getDisplayName();
    String getFullDisplayName();
    String getRelativeNameFrom(ItemGroup g);
    String getRelativeNameFrom(Item item);
    String getUrl();
    String getShortUrl();
    @Deprecated
    String getAbsoluteUrl();
    void onLoad(ItemGroup<? extends Item> parent, String name) throws IOException;
    void onCopiedFrom(Item src);
    void onCreatedFromScratch();
    void save() throws IOException;
    void delete() throws IOException, InterruptedException;
    PermissionGroup PERMISSIONS = new PermissionGroup(Item.class,Messages._Item_Permissions_Title());
    Permission CREATE = new Permission(PERMISSIONS, "Create", Messages._Item_CREATE_description(), Permission.CREATE, PermissionScope.ITEM_GROUP);
    Permission DELETE = new Permission(PERMISSIONS, "Delete", Messages._Item_DELETE_description(), Permission.DELETE, PermissionScope.ITEM);
    Permission CONFIGURE = new Permission(PERMISSIONS, "Configure", Messages._Item_CONFIGURE_description(), Permission.CONFIGURE, PermissionScope.ITEM);
    Permission READ = new Permission(PERMISSIONS, "Read", Messages._Item_READ_description(), Permission.READ, PermissionScope.ITEM);
    Permission DISCOVER = new Permission(PERMISSIONS, "Discover", Messages._AbstractProject_DiscoverPermission_Description(), READ, PermissionScope.ITEM);
    Permission EXTENDED_READ = new Permission(PERMISSIONS,"ExtendedRead", Messages._AbstractProject_ExtendedReadPermission_Description(), CONFIGURE, Boolean.getBoolean("hudson.security.ExtendedReadPermission"), new PermissionScope[]{PermissionScope.ITEM});
    Permission BUILD = new Permission(PERMISSIONS, "Build", Messages._AbstractProject_BuildPermission_Description(),  Permission.UPDATE, PermissionScope.ITEM);
    Permission WORKSPACE = new Permission(PERMISSIONS, "Workspace", Messages._AbstractProject_WorkspacePermission_Description(), Permission.READ, PermissionScope.ITEM);
    Permission WIPEOUT = new Permission(PERMISSIONS, "WipeOut", Messages._AbstractProject_WipeOutPermission_Description(), null, Functions.isWipeOutPermissionEnabled(), new PermissionScope[]{PermissionScope.ITEM});
    Permission CANCEL = new Permission(PERMISSIONS, "Cancel", Messages._AbstractProject_CancelPermission_Description(), BUILD, PermissionScope.ITEM);
}
