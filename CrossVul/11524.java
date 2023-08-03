
package org.elasticsearch.common.io;
import com.fasterxml.jackson.core.JsonLocation;
import com.google.common.collect.ImmutableMap;
import org.elasticsearch.common.Classes;
import org.elasticsearch.common.collect.IdentityHashSet;
import org.joda.time.DateTimeFieldType;
import java.io.*;
import java.net.*;
import java.util.*;
public class ThrowableObjectInputStream extends ObjectInputStream {
    private final ClassLoader classLoader;
    public ThrowableObjectInputStream(InputStream in) throws IOException {
        this(in, null);
    }
    public ThrowableObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }
    @Override
    protected void readStreamHeader() throws IOException, StreamCorruptedException {
        int version = readByte() & 0xFF;
        if (version != STREAM_VERSION) {
            throw new StreamCorruptedException(
                    "Unsupported version: " + version);
        }
    }
    @Override
    protected ObjectStreamClass readClassDescriptor()
            throws IOException, ClassNotFoundException {
        int type = read();
        if (type < 0) {
            throw new EOFException();
        }
        switch (type) {
            case ThrowableObjectOutputStream.TYPE_EXCEPTION:
                return ObjectStreamClass.lookup(Exception.class);
            case ThrowableObjectOutputStream.TYPE_STACKTRACEELEMENT:
                return ObjectStreamClass.lookup(StackTraceElement.class);
            case ThrowableObjectOutputStream.TYPE_FAT_DESCRIPTOR:
                return verify(super.readClassDescriptor());
            case ThrowableObjectOutputStream.TYPE_THIN_DESCRIPTOR:
                String className = readUTF();
                Class<?> clazz = loadClass(className);
                return verify(ObjectStreamClass.lookup(clazz));
            default:
                throw new StreamCorruptedException(
                        "Unexpected class descriptor type: " + type);
        }
    }
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        try {
            return loadClass(className);
        } catch (ClassNotFoundException ex) {
            return super.resolveClass(desc);
        }
    }
    protected Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz;
        ClassLoader classLoader = this.classLoader;
        if (classLoader == null) {
            classLoader = Classes.getDefaultClassLoader();
        }
        if (classLoader != null) {
            clazz = classLoader.loadClass(className);
        } else {
            clazz = Class.forName(className);
        }
        return clazz;
    }
    private static final Set<Class<?>> CLASS_WHITELIST;
    private static final Set<Package> PKG_WHITELIST;
    static {
        IdentityHashSet<Class<?>> classes = new IdentityHashSet<>();
        classes.add(String.class);
        classes.add(Inet6Address.class);
        classes.add(Inet4Address.class);
        classes.add(InetAddress.class);
        classes.add(InetSocketAddress.class);
        classes.add(SocketAddress.class);
        classes.add(StackTraceElement.class);
        classes.add(JsonLocation.class); 
        IdentityHashSet<Package> packages = new IdentityHashSet<>();
        packages.add(Integer.class.getPackage()); 
        packages.add(List.class.getPackage()); 
        packages.add(ImmutableMap.class.getPackage()); 
        packages.add(DateTimeFieldType.class.getPackage()); 
        CLASS_WHITELIST = Collections.unmodifiableSet(classes);
        PKG_WHITELIST = Collections.unmodifiableSet(packages);
    }
    private ObjectStreamClass verify(ObjectStreamClass streamClass) throws IOException, ClassNotFoundException {
        Class<?> aClass = resolveClass(streamClass);
        Package pkg = aClass.getPackage();
        if (aClass.isPrimitive() 
                || aClass.isArray() 
                || Throwable.class.isAssignableFrom(aClass)
                || CLASS_WHITELIST.contains(aClass) 
                || PKG_WHITELIST.contains(aClass.getPackage())
                || pkg.getName().startsWith("org.elasticsearch")) { 
            return streamClass;
        }
        throw new NotSerializableException(aClass.getName());
    }
}
