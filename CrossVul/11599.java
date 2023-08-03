
package org.ajax4jsf.resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class LookAheadObjectInputStream extends ObjectInputStream {
    private static final Map<String, Class<?>> PRIMITIVE_TYPES = new HashMap<String, Class<?>>(9, 1.0F);
    private static Set<Class> whitelistBaseClasses = new HashSet<Class>();
    private static Set<String> whitelistClassNameCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    static {
        PRIMITIVE_TYPES.put("bool", Boolean.TYPE);
        PRIMITIVE_TYPES.put("byte", Byte.TYPE);
        PRIMITIVE_TYPES.put("char", Character.TYPE);
        PRIMITIVE_TYPES.put("short", Short.TYPE);
        PRIMITIVE_TYPES.put("int", Integer.TYPE);
        PRIMITIVE_TYPES.put("long", Long.TYPE);
        PRIMITIVE_TYPES.put("float", Float.TYPE);
        PRIMITIVE_TYPES.put("double", Double.TYPE);
        PRIMITIVE_TYPES.put("void", Void.TYPE);
        whitelistClassNameCache.add(new Object[0].getClass().getName());
        whitelistClassNameCache.add(new String[0].getClass().getName());
        whitelistClassNameCache.add(new Boolean[0].getClass().getName());
        whitelistClassNameCache.add(new Byte[0].getClass().getName());
        whitelistClassNameCache.add(new Character[0].getClass().getName());
        whitelistClassNameCache.add(new Short[0].getClass().getName());
        whitelistClassNameCache.add(new Integer[0].getClass().getName());
        whitelistClassNameCache.add(new Long[0].getClass().getName());
        whitelistClassNameCache.add(new Float[0].getClass().getName());
        whitelistClassNameCache.add(new Double[0].getClass().getName());
        whitelistClassNameCache.add(new Void[0].getClass().getName());
        whitelistBaseClasses.add(String.class);
        whitelistBaseClasses.add(Boolean.class);
        whitelistBaseClasses.add(Byte.class);
        whitelistBaseClasses.add(Character.class);
        whitelistBaseClasses.add(Number.class);
        loadWhitelist();
    }
    public LookAheadObjectInputStream(InputStream in) throws IOException {
        super(in);
    }
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Class<?> primitiveType = PRIMITIVE_TYPES.get(desc.getName());
        if (primitiveType != null) {
            return primitiveType;
        }
        if (!isClassValid(desc.getName())) {
            throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
        }
        return super.resolveClass(desc);
    }
    boolean isClassValid(String requestedClassName) {
        if (whitelistClassNameCache.contains(requestedClassName)) {
            return true;
        }
        try {
            Class<?> requestedClass = Class.forName(requestedClassName);
            for (Class baseClass : whitelistBaseClasses ) {
                if (baseClass.isAssignableFrom(requestedClass)) {
                    whitelistClassNameCache.add(requestedClassName);
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }
    static void loadWhitelist() {
        Properties whitelistProperties = new Properties();
        InputStream stream = null;
        try {
            stream =  LookAheadObjectInputStream.class.getResourceAsStream("resource-serialization.properties");
            whitelistProperties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading the ResourceBuilder.properties file", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Error closing the ResourceBuilder.properties file", e);
                }
            }
        }
        for (String baseClassName : whitelistProperties.getProperty("whitelist").split(",")) {
            try {
                Class<?> baseClass = Class.forName(baseClassName);
                whitelistBaseClasses.add(baseClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to load whiteList class " + baseClassName, e);
            }
        }
    }
}
