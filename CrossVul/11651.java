
package io.milton.http;
import io.milton.resource.AccessControlledResource;
import io.milton.resource.AccessControlledResource.Priviledge;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
public class AclUtils {
    public static boolean containsPriviledge(AccessControlledResource.Priviledge required, Iterable<AccessControlledResource.Priviledge> privs) {
        if( privs == null ) {
            return false;
        }
        for (AccessControlledResource.Priviledge p : privs) {
            if (p.equals(required)) {
                return true;
            }
            if( containsPriviledge(required, p.contains)) {
                return true;
            }
        }
        return false;
    }
    public static Set<AccessControlledResource.Priviledge> asSet(AccessControlledResource.Priviledge ... privs) {
        Set<AccessControlledResource.Priviledge> set = new HashSet<AccessControlledResource.Priviledge>(privs.length);
        set.addAll(Arrays.asList(privs));
        return set;
    }
    public static Set<AccessControlledResource.Priviledge> expand(Iterable<AccessControlledResource.Priviledge> privs) {
        Set<AccessControlledResource.Priviledge> set = new HashSet<AccessControlledResource.Priviledge>();
        _expand(privs, set);
        return set;
    }
    private static void _expand(Iterable<AccessControlledResource.Priviledge> privs, Set<AccessControlledResource.Priviledge> output) {
        if( privs == null ) {
            return ;
        }
        for( Priviledge p : privs ) {
            output.add(p);
            _expand(p.contains, output);
        }
    }
}
