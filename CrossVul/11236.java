
package org.dom4j;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.regex.Pattern;
import org.dom4j.tree.QNameCache;
import org.dom4j.util.SingletonStrategy;
public class QName implements Serializable {
    private static SingletonStrategy<QNameCache> singleton = null;
    private static final String NAME_START_CHAR = "_A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD";
    private static final String NAME_CHAR = NAME_START_CHAR + "-.0-9\u00B7\u0300-\u036F\u203F-\u2040";
    private static final String NCNAME = "["+NAME_START_CHAR+"]["+NAME_CHAR+"]*";
    private static final Pattern RE_NAME = Pattern.compile("[:"+NAME_START_CHAR+"][:"+NAME_CHAR+"]*");
    private static final Pattern RE_NCNAME = Pattern.compile(NCNAME);
    private static final Pattern RE_QNAME = Pattern.compile("(?:"+NCNAME+":)?"+NCNAME);
    static {
        try {
            String defaultSingletonClass = "org.dom4j.util.SimpleSingleton";
            Class<SingletonStrategy> clazz = null;
            try {
                String singletonClass = defaultSingletonClass;
                singletonClass = System.getProperty(
                        "org.dom4j.QName.singleton.strategy", singletonClass);
                clazz = (Class<SingletonStrategy>) Class.forName(singletonClass);
            } catch (Exception exc1) {
                try {
                    String singletonClass = defaultSingletonClass;
                    clazz = (Class<SingletonStrategy>) Class.forName(singletonClass);
                } catch (Exception exc2) {
                }
            }
            singleton = clazz.newInstance();
            singleton.setSingletonClassName(QNameCache.class.getName());
        } catch (Exception exc3) {
        }
    }
    private String name;
    private String qualifiedName;
    private transient Namespace namespace;
    private int hashCode;
    private DocumentFactory documentFactory;
    public QName(String name) {
        this(name, Namespace.NO_NAMESPACE);
    }
    public QName(String name, Namespace namespace) {
        this.name = (name == null) ? "" : name;
        this.namespace = (namespace == null) ? Namespace.NO_NAMESPACE
                : namespace;
        if (this.namespace.equals(Namespace.NO_NAMESPACE)) {
            validateName(this.name);
        } else {
            validateNCName(this.name);
        }
    }
    public QName(String name, Namespace namespace, String qualifiedName) {
        this.name = (name == null) ? "" : name;
        this.qualifiedName = qualifiedName;
        this.namespace = (namespace == null) ? Namespace.NO_NAMESPACE
                : namespace;
        validateNCName(this.name);
        validateQName(this.qualifiedName);
    }
    public static QName get(String name) {
        return getCache().get(name);
    }
    public static QName get(String name, Namespace namespace) {
        return getCache().get(name, namespace);
    }
    public static QName get(String name, String prefix, String uri) {
        if (((prefix == null) || (prefix.length() == 0)) && (uri == null)) {
            return QName.get(name);
        } else if ((prefix == null) || (prefix.length() == 0)) {
            return getCache().get(name, Namespace.get(uri));
        } else if (uri == null) {
            return QName.get(name);
        } else {
            return getCache().get(name, Namespace.get(prefix, uri));
        }
    }
    public static QName get(String qualifiedName, String uri) {
        if (uri == null) {
            return getCache().get(qualifiedName);
        } else {
            return getCache().get(qualifiedName, uri);
        }
    }
    public static QName get(String localName, Namespace namespace,
            String qualifiedName) {
        return getCache().get(localName, namespace, qualifiedName);
    }
    public String getName() {
        return name;
    }
    public String getQualifiedName() {
        if (qualifiedName == null) {
            String prefix = getNamespacePrefix();
            if ((prefix != null) && (prefix.length() > 0)) {
                qualifiedName = prefix + ":" + name;
            } else {
                qualifiedName = name;
            }
        }
        return qualifiedName;
    }
    public Namespace getNamespace() {
        return namespace;
    }
    public String getNamespacePrefix() {
        if (namespace == null) {
            return "";
        }
        return namespace.getPrefix();
    }
    public String getNamespaceURI() {
        if (namespace == null) {
            return "";
        }
        return namespace.getURI();
    }
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = getName().hashCode() ^ getNamespaceURI().hashCode();
            if (hashCode == 0) {
                hashCode = 0xbabe;
            }
        }
        return hashCode;
    }
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof QName) {
            QName that = (QName) object;
            if (hashCode() == that.hashCode()) {
                return getName().equals(that.getName())
                        && getNamespaceURI().equals(that.getNamespaceURI());
            }
        }
        return false;
    }
    public String toString() {
        return super.toString() + " [name: " + getName() + " namespace: \""
                + getNamespace() + "\"]";
    }
    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }
    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(namespace.getPrefix());
        out.writeObject(namespace.getURI());
    }
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        String prefix = (String) in.readObject();
        String uri = (String) in.readObject();
        namespace = Namespace.get(prefix, uri);
    }
    private static QNameCache getCache() {
        QNameCache cache = singleton.instance();
        return cache;
    }
    private static void validateName(String name) {
        if (!RE_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format("Illegal character in name: '%s'.", name));
        }
    }
    protected static void validateNCName(String ncname) {
        if (!RE_NCNAME.matcher(ncname).matches()) {
            throw new IllegalArgumentException(String.format("Illegal character in local name: '%s'.", ncname));
        }
    }
    private static void validateQName(String qname) {
        if (!RE_QNAME.matcher(qname).matches()) {
            throw new IllegalArgumentException(String.format("Illegal character in qualified name: '%s'.", qname));
        }
    }
}
