
package hudson.diagnosis;
import com.google.common.base.Predicate;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import hudson.Extension;
import hudson.XmlFile;
import hudson.model.AdministrativeMonitor;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.ManagementLink;
import hudson.model.Run;
import hudson.model.Saveable;
import hudson.model.listeners.ItemListener;
import hudson.model.listeners.RunListener;
import hudson.model.listeners.SaveableListener;
import hudson.security.ACL;
import hudson.util.RobustReflectionConverter;
import hudson.util.VersionNumber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;
import jenkins.model.Jenkins;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;
@Extension
public class OldDataMonitor extends AdministrativeMonitor {
    private static final Logger LOGGER = Logger.getLogger(OldDataMonitor.class.getName());
    private ConcurrentMap<SaveableReference,VersionRange> data = new ConcurrentHashMap<SaveableReference,VersionRange>();
    static OldDataMonitor get(Jenkins j) {
        return (OldDataMonitor) j.getAdministrativeMonitor("OldData");
    }
    public OldDataMonitor() {
        super("OldData");
    }
    @Override
    public String getDisplayName() {
        return Messages.OldDataMonitor_DisplayName();
    }
    public boolean isActivated() {
        return !data.isEmpty();
    }
    public Map<Saveable,VersionRange> getData() {
        Map<Saveable,VersionRange> r = new HashMap<Saveable,VersionRange>();
        for (Map.Entry<SaveableReference,VersionRange> entry : this.data.entrySet()) {
            Saveable s = entry.getKey().get();
            if (s != null) {
                r.put(s, entry.getValue());
            }
        }
        return r;
    }
    private static void remove(Saveable obj, boolean isDelete) {
        Jenkins j = Jenkins.getInstance();
        if (j != null) {
            OldDataMonitor odm = get(j);
            SecurityContext oldContext = ACL.impersonate(ACL.SYSTEM);
            try {
                odm.data.remove(referTo(obj));
                if (isDelete && obj instanceof Job<?, ?>) {
                    for (Run r : ((Job<?, ?>) obj).getBuilds()) {
                        odm.data.remove(referTo(r));
                    }
                }
            } finally {
                SecurityContextHolder.setContext(oldContext);
            }
        }
    }
    @Extension
    public static final SaveableListener changeListener = new SaveableListener() {
        @Override
        public void onChange(Saveable obj, XmlFile file) {
            remove(obj, false);
        }
    };
    @Extension
    public static final ItemListener itemDeleteListener = new ItemListener() {
        @Override
        public void onDeleted(Item item) {
            remove(item, true);
        }
    };
    @Extension
    public static final RunListener<Run> runDeleteListener = new RunListener<Run>() {
        @Override
        public void onDeleted(Run run) {
            remove(run, true);
        }
    };
    public static void report(Saveable obj, String version) {
        OldDataMonitor odm = get(Jenkins.getInstance());
        try {
            SaveableReference ref = referTo(obj);
            while (true) {
                VersionRange vr = odm.data.get(ref);
                if (vr != null && odm.data.replace(ref, vr, new VersionRange(vr, version, null))) {
                    break;
                } else if (odm.data.putIfAbsent(ref, new VersionRange(null, version, null)) == null) {
                    break;
                }
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, "Bad parameter given to OldDataMonitor", ex);
        }
    }
    public static void report(UnmarshallingContext context, String version) {
        RobustReflectionConverter.addErrorInContext(context, new ReportException(version));
    }
    private static class ReportException extends Exception {
        private String version;
        private ReportException(String version) {
            this.version = version;
        }
    }
    public static void report(Saveable obj, Collection<Throwable> errors) {
        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (Throwable e : errors) {
            if (e instanceof ReportException) {
                report(obj, ((ReportException)e).version);
            } else {
                if (++i > 1) buf.append(", ");
                buf.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());
            }
        }
        if (buf.length() == 0) return;
        Jenkins j = Jenkins.getInstance();
        if (j == null) {
            for (Throwable t : errors) {
                LOGGER.log(Level.WARNING, "could not read " + obj + " (and Jenkins did not start up)", t);
            }
            return;
        }
        OldDataMonitor odm = get(j);
        SaveableReference ref = referTo(obj);
        while (true) {
            VersionRange vr = odm.data.get(ref);
            if (vr != null && odm.data.replace(ref, vr, new VersionRange(vr, null, buf.toString()))) {
                break;
            } else if (odm.data.putIfAbsent(ref, new VersionRange(null, null, buf.toString())) == null) {
                break;
            }
        }
    }
    public static class VersionRange {
        private static VersionNumber currentVersion = Jenkins.getVersion();
        final VersionNumber min;
        final VersionNumber max;
        final boolean single;
        final public String extra;
        public VersionRange(VersionRange previous, String version, String extra) {
            if (previous == null) {
                min = max = version != null ? new VersionNumber(version) : null;
                this.single = true;
                this.extra = extra;
            } else if (version == null) {
                min = previous.min;
                max = previous.max;
                single = previous.single;
                this.extra = extra;
            } else {
                VersionNumber ver = new VersionNumber(version);
                if (previous.min == null || ver.isOlderThan(previous.min)) {
                    this.min = ver;
                } else {
                    this.min = previous.min;
                }
                if (previous.max == null || ver.isNewerThan(previous.max)) {
                    this.max = ver;
                } else {
                    this.max = previous.max;
                }
                this.single = this.max.isNewerThan(this.min);
                this.extra = extra;
            }
        }
        @Override
        public String toString() {
            return min==null ? "" : min.toString() + (single ? "" : " - " + max.toString());
        }
        public boolean isOld(int threshold) {
            return currentVersion != null && min != null && (currentVersion.digit(0) > min.digit(0)
                    || (currentVersion.digit(0) == min.digit(0)
                    && currentVersion.digit(1) - min.digit(1) >= threshold));
        }
    }
    @Restricted(NoExternalUse.class)
    public Iterator<VersionNumber> getVersionList() {
        TreeSet<VersionNumber> set = new TreeSet<VersionNumber>();
        for (VersionRange vr : data.values()) {
            if (vr.max != null) {
                set.add(vr.max);
            }
        }
        return set.iterator();
    }
    @RequirePOST
    public HttpResponse doAct(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if (req.hasParameter("no")) {
            disable(true);
            return HttpResponses.redirectViaContextPath("/manage");
        } else {
            return new HttpRedirect("manage");
        }
    }
    @RequirePOST
    public HttpResponse doUpgrade(StaplerRequest req, StaplerResponse rsp) {
        final String thruVerParam = req.getParameter("thruVer");
        final VersionNumber thruVer = thruVerParam.equals("all") ? null : new VersionNumber(thruVerParam);
        saveAndRemoveEntries(new Predicate<Map.Entry<SaveableReference, VersionRange>>() {
            @Override
            public boolean apply(Map.Entry<SaveableReference, VersionRange> entry) {
                VersionNumber version = entry.getValue().max;
                return version != null && (thruVer == null || !version.isNewerThan(thruVer));
            }
        });
        return HttpResponses.forwardToPreviousPage();
    }
    @RequirePOST
    public HttpResponse doDiscard(StaplerRequest req, StaplerResponse rsp) {
        saveAndRemoveEntries( new Predicate<Map.Entry<SaveableReference,VersionRange>>() {
            @Override
            public boolean apply(Map.Entry<SaveableReference, VersionRange> entry) {
                return entry.getValue().max == null;
            }
        });
        return HttpResponses.forwardToPreviousPage();
    }
    private void saveAndRemoveEntries(Predicate<Map.Entry<SaveableReference, VersionRange>> matchingPredicate) {
        List<SaveableReference> removed = new ArrayList<SaveableReference>();
        for (Map.Entry<SaveableReference,VersionRange> entry : data.entrySet()) {
            if (matchingPredicate.apply(entry)) {
                Saveable s = entry.getKey().get();
                if (s != null) {
                    try {
                        s.save();
                    } catch (Exception x) {
                        LOGGER.log(Level.WARNING, "failed to save " + s, x);
                    }
                }
                removed.add(entry.getKey());
            }
        }
        data.keySet().removeAll(removed);
    }
    public HttpResponse doIndex(StaplerResponse rsp) throws IOException {
        return new HttpRedirect("manage");
    }
    private interface SaveableReference {
        @CheckForNull Saveable get();
    }
    private static SaveableReference referTo(Saveable s) {
        if (s instanceof Run) {
            Job parent = ((Run) s).getParent();
            if (Jenkins.getInstance().getItemByFullName(parent.getFullName()) == parent) {
                return new RunSaveableReference((Run) s);
            }
        }
        return new SimpleSaveableReference(s);
    }
    private static final class SimpleSaveableReference implements SaveableReference {
        private final Saveable instance;
        SimpleSaveableReference(Saveable instance) {
            this.instance = instance;
        }
        @Override public Saveable get() {
            return instance;
        }
        @Override public int hashCode() {
            return instance.hashCode();
        }
        @Override public boolean equals(Object obj) {
            return obj instanceof SimpleSaveableReference && instance.equals(((SimpleSaveableReference) obj).instance);
        }
    }
    private static final class RunSaveableReference implements SaveableReference {
        private final String id;
        RunSaveableReference(Run<?,?> r) {
            id = r.getExternalizableId();
        }
        @Override public Saveable get() {
            try {
                return Run.fromExternalizableId(id);
            } catch (IllegalArgumentException x) {
                LOGGER.log(Level.FINE, null, x);
                return null;
            }
        }
        @Override public int hashCode() {
            return id.hashCode();
        }
        @Override public boolean equals(Object obj) {
            return obj instanceof RunSaveableReference && id.equals(((RunSaveableReference) obj).id);
        }
    }
    @Extension
    public static class ManagementLinkImpl extends ManagementLink {
        @Override
        public String getIconFileName() {
            return "document.png";
        }
        @Override
        public String getUrlName() {
            return "administrativeMonitor/OldData/";
        }
        @Override
        public String getDescription() {
            return Messages.OldDataMonitor_Description();
        }
        public String getDisplayName() {
            return Messages.OldDataMonitor_DisplayName();
        }
    }
}
