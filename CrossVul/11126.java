
package hudson.util;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import hudson.FilePath;
import hudson.Functions;
import jenkins.model.Jenkins;
import hudson.remoting.AsyncFutureImpl;
import hudson.remoting.Callable;
import hudson.remoting.DelegatingCallable;
import hudson.remoting.Future;
import hudson.remoting.VirtualChannel;
import hudson.security.AccessControlled;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
public final class RemotingDiagnostics {
    public static Map<Object,Object> getSystemProperties(VirtualChannel channel) throws IOException, InterruptedException {
        if(channel==null)
            return Collections.<Object,Object>singletonMap("N/A","N/A");
        return channel.call(new GetSystemProperties());
    }
    private static final class GetSystemProperties implements Callable<Map<Object,Object>,RuntimeException> {
        public Map<Object,Object> call() {
            return new TreeMap<Object,Object>(System.getProperties());
        }
        private static final long serialVersionUID = 1L;
    }
    public static Map<String,String> getThreadDump(VirtualChannel channel) throws IOException, InterruptedException {
        if(channel==null)
            return Collections.singletonMap("N/A","N/A");
        return channel.call(new GetThreadDump());
    }
    public static Future<Map<String,String>> getThreadDumpAsync(VirtualChannel channel) throws IOException, InterruptedException {
        if(channel==null)
            return new AsyncFutureImpl<Map<String, String>>(Collections.singletonMap("N/A","offline"));
        return channel.callAsync(new GetThreadDump());
    }
    private static final class GetThreadDump implements Callable<Map<String,String>,RuntimeException> {
        public Map<String,String> call() {
            Map<String,String> r = new LinkedHashMap<String,String>();
            try {
                ThreadInfo[] data = Functions.getThreadInfos();
                Functions.ThreadGroupMap map = Functions.sortThreadsAndGetGroupMap(data);
                for (ThreadInfo ti : data)
                    r.put(ti.getThreadName(),Functions.dumpThreadInfo(ti,map));
            } catch (LinkageError _) {
                r.clear();
                for (Map.Entry<Thread,StackTraceElement[]> t : Functions.dumpAllThreads().entrySet()) {
                    StringBuilder buf = new StringBuilder();
                    for (StackTraceElement e : t.getValue())
                        buf.append(e).append('\n');
                    r.put(t.getKey().getName(),buf.toString());
                }
            }
            return r;
        }
        private static final long serialVersionUID = 1L;
    }
    public static String executeGroovy(String script, VirtualChannel channel) throws IOException, InterruptedException {
        return channel.call(new Script(script));
    }
    private static final class Script implements DelegatingCallable<String,RuntimeException> {
        private final String script;
        private transient ClassLoader cl;
        private Script(String script) {
            this.script = script;
            cl = getClassLoader();
        }
        public ClassLoader getClassLoader() {
            return Jenkins.getInstance().getPluginManager().uberClassLoader;
        }
        public String call() throws RuntimeException {
            if (cl==null)       cl = Thread.currentThread().getContextClassLoader();
            CompilerConfiguration cc = new CompilerConfiguration();
            cc.addCompilationCustomizers(new ImportCustomizer().addStarImports(
                    "jenkins",
                    "jenkins.model",
                    "hudson",
                    "hudson.model"));
            GroovyShell shell = new GroovyShell(cl,new Binding(),cc);
            StringWriter out = new StringWriter();
            PrintWriter pw = new PrintWriter(out);
            shell.setVariable("out", pw);
            try {
                Object output = shell.evaluate(script);
                if(output!=null)
                pw.println("Result: "+output);
            } catch (Throwable t) {
                t.printStackTrace(pw);
            }
            return out.toString();
        }
    }
    public static FilePath getHeapDump(VirtualChannel channel) throws IOException, InterruptedException {
        return channel.call(new Callable<FilePath, IOException>() {
            public FilePath call() throws IOException {
                final File hprof = File.createTempFile("hudson-heapdump", "hprof");
                hprof.delete();
                try {
                    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
                    server.invoke(new ObjectName("com.sun.management:type=HotSpotDiagnostic"), "dumpHeap",
                            new Object[]{hprof.getAbsolutePath(), true}, new String[]{String.class.getName(), boolean.class.getName()});
                    return new FilePath(hprof);
                } catch (JMException e) {
                    throw new IOException2(e);
                }
            }
            private static final long serialVersionUID = 1L;
        });
    }
    public static class HeapDump {
        private final AccessControlled owner;
        private final VirtualChannel channel;
        public HeapDump(AccessControlled owner, VirtualChannel channel) {
            this.owner = owner;
            this.channel = channel;
        }
        public void doIndex(StaplerResponse rsp) throws IOException {
            rsp.sendRedirect("heapdump.hprof");
        }
        @WebMethod(name="heapdump.hprof")
        public void doHeapDump(StaplerRequest req, StaplerResponse rsp) throws IOException, InterruptedException {
            owner.checkPermission(Jenkins.RUN_SCRIPTS);
            rsp.setContentType("application/octet-stream");
            FilePath dump = obtain();
            try {
                dump.copyTo(rsp.getCompressedOutputStream(req));
            } finally {
                dump.delete();
            }
        }
        public FilePath obtain() throws IOException, InterruptedException {
            return RemotingDiagnostics.getHeapDump(channel);
        }
    }
}
