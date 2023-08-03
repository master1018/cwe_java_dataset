
package org.elasticsearch.common.io;
import org.elasticsearch.common.Classes;
import java.io.*;
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
                return super.readClassDescriptor();
            case ThrowableObjectOutputStream.TYPE_THIN_DESCRIPTOR:
                String className = readUTF();
                Class<?> clazz = loadClass(className);
                return ObjectStreamClass.lookup(clazz);
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
}
