package hudson.util;
import com.trilead.ssh2.crypto.Base64;
import hudson.model.TaskListener;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.HashSet;
import java.util.Set;
public class SecretRewriter {
    private final Cipher cipher;
    private final SecretKey key;
    private int count;
    private Set<String> callstack = new HashSet<String>();
    public SecretRewriter() throws GeneralSecurityException {
        cipher = Secret.getCipher("AES");
        key = HistoricalSecrets.getLegacyKey();
    }
    @Deprecated
    public SecretRewriter(File backupDirectory) throws GeneralSecurityException {
        this();
    }
    private String tryRewrite(String s) throws IOException, InvalidKeyException {
        if (s.length()<24)
            return s;   
        if (!isBase64(s))
            return s;   
        byte[] in;
        try {
            in = Base64.decode(s.toCharArray());
        } catch (IOException e) {
            return s;   
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        Secret sec = HistoricalSecrets.tryDecrypt(cipher, in);
        if(sec!=null) 
            return sec.getEncryptedValue(); 
        else 
            return s;
    }
    @Deprecated
    public boolean rewrite(File f, File backup) throws InvalidKeyException, IOException {
        return rewrite(f);
    }
    public boolean rewrite(File f) throws InvalidKeyException, IOException {
        AtomicFileWriter w = new AtomicFileWriter(f, "UTF-8");
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(w));
            boolean modified = false; 
            try {
                FileInputStream fin = new FileInputStream(f);
                try {
                    BufferedReader r = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                    String line;
                    StringBuilder buf = new StringBuilder();
                    while ((line=r.readLine())!=null) {
                        int copied=0;
                        buf.setLength(0);
                        while (true) {
                            int sidx = line.indexOf('>',copied);
                            if (sidx<0) break;
                            int eidx = line.indexOf('<',sidx);
                            if (eidx<0) break;
                            String elementText = line.substring(sidx+1,eidx);
                            String replacement = tryRewrite(elementText);
                            if (!replacement.equals(elementText))
                                modified = true;
                            buf.append(line.substring(copied,sidx+1));
                            buf.append(replacement);
                            copied = eidx;
                        }
                        buf.append(line.substring(copied));
                        out.println(buf.toString());
                    }
                } finally {
                    fin.close();
                }
            } finally {
                out.close();
            }
            if (modified) {
                w.commit();
            }
            return modified;
        } finally {
            w.abort();
        }
    }
    public synchronized int rewriteRecursive(File dir, TaskListener listener) throws InvalidKeyException {
        return rewriteRecursive(dir,"",listener);
    }
    private int rewriteRecursive(File dir, String relative, TaskListener listener) throws InvalidKeyException {
        String canonical;
        try {
            canonical = dir.getCanonicalPath();
        } catch (IOException e) {
            canonical = dir.getAbsolutePath(); 
        }
        if (!callstack.add(canonical)) {
            listener.getLogger().println("Cycle detected: "+dir);
            return 0;
        }
        try {
            File[] children = dir.listFiles();
            if (children==null)     return 0;
            int rewritten=0;
            for (File child : children) {
                String cn = child.getName();
                if (cn.endsWith(".xml")) {
                    if ((count++)%100==0)
                        listener.getLogger().println("Scanning "+child);
                    try {
                        if (rewrite(child)) {
                            listener.getLogger().println("Rewritten "+child);
                            rewritten++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace(listener.error("Failed to rewrite "+child));
                    }
                }
                if (child.isDirectory()) {
                    if (!isIgnoredDir(child))
                        rewritten += rewriteRecursive(child,
                                relative.length()==0 ? cn : relative+'/'+ cn,
                                listener);
                }
            }
            return rewritten;
        } finally {
            callstack.remove(canonical);
        }
    }
    protected boolean isIgnoredDir(File dir) {
        String n = dir.getName();
        return n.equals("workspace") || n.equals("artifacts")
            || n.equals("plugins") 
            || n.equals(".") || n.equals("..");
    }
    private static boolean isBase64(char ch) {
        return 0<=ch && ch<128 && IS_BASE64[ch];
    }
    private static boolean isBase64(String s) {
        for (int i=0; i<s.length(); i++)
            if (!isBase64(s.charAt(i)))
                return false;
        return true;
    }
    private static final boolean[] IS_BASE64 = new boolean[128];
    static {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        for (int i=0; i<chars.length();i++)
            IS_BASE64[chars.charAt(i)] = true;
    }
}