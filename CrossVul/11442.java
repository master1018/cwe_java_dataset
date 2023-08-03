
package org.fusesource.hawtjni.runtime;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
public class Library {
    static final String SLASH = System.getProperty("file.separator");
    final private String name;
    final private String version;
    final private ClassLoader classLoader;
    private boolean loaded;
    public Library(String name) {
        this(name, null, null);
    }
    public Library(String name, Class<?> clazz) {
        this(name, version(clazz), clazz.getClassLoader());
    }
    public Library(String name, String version) {
        this(name, version, null);
    }
    public Library(String name, String version, ClassLoader classLoader) {
        if( name == null ) {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
        this.version = version;
        this.classLoader= classLoader;
    }
    private static String version(Class<?> clazz) {
        try {
            return clazz.getPackage().getImplementationVersion();
        } catch (Throwable e) {
        }
        return null;
    }
    public static String getOperatingSystem() {
        String name = System.getProperty("os.name").toLowerCase().trim();
        if( name.startsWith("linux") ) {
            return "linux";
        }
        if( name.startsWith("mac os x") ) {
            return "osx";
        }
        if( name.startsWith("win") ) {
            return "windows";
        }
        return name.replaceAll("\\W+", "_");
    }
    public static String getPlatform() {
        return getOperatingSystem()+getBitModel();
    }
    public static int getBitModel() {
        String prop = System.getProperty("sun.arch.data.model"); 
        if (prop == null) {
            prop = System.getProperty("com.ibm.vm.bitmode");
        }
        if( prop!=null ) {
            return Integer.parseInt(prop);
        }
        return -1; 
    }
    synchronized public void load() {
        if( loaded ) {
            return;
        }
        doLoad();
        loaded = true;
    }
    private void doLoad() {
        String version = System.getProperty("library."+name+".version"); 
        if (version == null) {
            version = this.version; 
        }
        ArrayList<String> errors = new ArrayList<String>();
        String customPath = System.getProperty("library."+name+".path");
        if (customPath != null) {
            if( version!=null && load(errors, file(customPath, map(name + "-" + version))) ) 
                return;
            if( load(errors, file(customPath, map(name))) )
                return;
        }
        if( version!=null && load(errors, name + getBitModel() + "-" + version) ) 
            return;        
        if( version!=null && load(errors, name + "-" + version) ) 
            return;        
        if( load(errors, name ) )
            return;
        if( classLoader!=null ) {
            if( exractAndLoad(errors, version, customPath, getPlatformSpecifcResourcePath()) ) 
                return;
            if( exractAndLoad(errors, version, customPath, getOperatingSystemSpecifcResourcePath()) ) 
                return;
            if( exractAndLoad(errors, version, customPath, getResorucePath()) )
                return;
        }
        throw new UnsatisfiedLinkError("Could not load library. Reasons: " + errors.toString()); 
    }
    final public String getOperatingSystemSpecifcResourcePath() {
        return getPlatformSpecifcResourcePath(getOperatingSystem());
    }
    final public String getPlatformSpecifcResourcePath() {
        return getPlatformSpecifcResourcePath(getPlatform());
    }
    final public String getPlatformSpecifcResourcePath(String platform) {
        return "META-INF/native/"+platform+"/"+map(name);
    }
    final public String getResorucePath() {
        return "META-INF/native/"+map(name);
    }
    final public String getLibraryFileName() {
        return map(name);
    }
    private boolean exractAndLoad(ArrayList<String> errors, String version, String customPath, String resourcePath) {
        URL resource = classLoader.getResource(resourcePath);
        if( resource !=null ) {
            String libName = name + "-" + getBitModel();
            if( version !=null) {
                libName += "-" + version;
            }
            String []libNameParts = map(libName).split("\\.");
            String prefix = libNameParts[0]+"-";
            String suffix = "."+libNameParts[1];
            if( customPath!=null ) {
                File target = extract(errors, resource, prefix, suffix, file(customPath));
                if( target!=null ) {
                    if( load(errors, target) ) {
                        return true;
                    }
                }
            }
            customPath = System.getProperty("java.io.tmpdir");
            File target = extract(errors, resource, prefix, suffix, file(customPath));
            if( target!=null ) {
                if( load(errors, target) ) {
                    return true;
                }
            }
        }
        return false;
    }
    private File file(String ...paths) {
        File rc = null ;
        for (String path : paths) {
            if( rc == null ) {
                rc = new File(path);
            } else {
                rc = new File(rc, path);
            }
        }
        return rc;
    }
    private String map(String libName) {
        libName = System.mapLibraryName(libName);
        String ext = ".dylib"; 
        if (libName.endsWith(ext)) {
            libName = libName.substring(0, libName.length() - ext.length()) + ".jnilib"; 
        }
        return libName;
    }
    private File extract(ArrayList<String> errors, URL source, String prefix, String suffix, File directory) {
        File target = null;
        try {
            FileOutputStream os = null;
            InputStream is = null;
            try {
                target = File.createTempFile(prefix, suffix, directory);
                is = source.openStream();
                if (is != null) {
                    byte[] buffer = new byte[4096];
                    os = new FileOutputStream(target);
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                    chmod("755", target);
                }
                target.deleteOnExit();
                return target;
            } finally {
                close(os);
                close(is);
            }
        } catch (Throwable e) {
            if( target!=null ) {
                target.delete();
            }
            errors.add(e.getMessage());
        }
        return null;
    }
    static private void close(Closeable file) {
        if(file!=null) {
            try {
                file.close();
            } catch (Exception ignore) {
            }
        }
    }
    private void chmod(String permision, File path) {
        if (getPlatform().startsWith("windows"))
            return; 
        try {
            Runtime.getRuntime().exec(new String[] { "chmod", permision, path.getCanonicalPath() }).waitFor(); 
        } catch (Throwable e) {
        }
    }
    private boolean load(ArrayList<String> errors, File lib) {
        try {
            System.load(lib.getPath());
            return true;
        } catch (UnsatisfiedLinkError e) {
            errors.add(e.getMessage());
        }
        return false;
    }
    private boolean load(ArrayList<String> errors, String lib) {
        try {
            System.loadLibrary(lib);
            return true;
        } catch (UnsatisfiedLinkError e) {
            errors.add(e.getMessage());
        }
        return false;
    }
}
