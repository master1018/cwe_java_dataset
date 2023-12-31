
package org.dom4j.tree;
import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;
import java.util.*;
public class QNameCache {
    protected Map<String, QName> noNamespaceCache = Collections.synchronizedMap(new WeakHashMap<String, QName>());
    protected Map<Namespace, Map<String, QName>> namespaceCache = Collections.synchronizedMap(new WeakHashMap<Namespace, Map<String, QName>>());
    private DocumentFactory documentFactory;
    public QNameCache() {
    }
    public QNameCache(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }
    public List<QName> getQNames() {
        List<QName> answer = new ArrayList<QName>();
        answer.addAll(noNamespaceCache.values());
        for (Map<String, QName> map : namespaceCache.values()) {
            answer.addAll(map.values());
        }
        return answer;
    }
    public QName get(String name) {
        QName answer = null;
        if (name != null) {
            answer = noNamespaceCache.get(name);
        } else {
            name = "";
        }
        if (answer == null) {
            answer = createQName(name);
            answer.setDocumentFactory(documentFactory);
            noNamespaceCache.put(name, answer);
        }
        return answer;
    }
    public QName get(String name, Namespace namespace) {
        Map<String, QName> cache = getNamespaceCache(namespace);
        QName answer = null;
        if (name != null) {
            answer = cache.get(name);
        } else {
            name = "";
        }
        if (answer == null) {
            answer = createQName(name, namespace);
            answer.setDocumentFactory(documentFactory);
            cache.put(name, answer);
        }
        return answer;
    }
    public QName get(String localName, Namespace namespace, String qName) {
        Map<String, QName> cache = getNamespaceCache(namespace);
        QName answer = null;
        if (localName != null) {
            answer = cache.get(localName);
        } else {
            localName = "";
        }
        if (answer == null) {
            answer = createQName(localName, namespace, qName);
            answer.setDocumentFactory(documentFactory);
            cache.put(localName, answer);
        }
        return answer;
    }
    public QName get(String qualifiedName, String uri) {
        int index = qualifiedName.indexOf(':');
        if (index < 0) {
            return get(qualifiedName, Namespace.get(uri));
        } else {
            String name = qualifiedName.substring(index + 1);
            String prefix = qualifiedName.substring(0, index);
            return get(name, Namespace.get(prefix, uri));
        }
    }
    public QName intern(QName qname) {
        return get(qname.getName(), qname.getNamespace(), qname
                .getQualifiedName());
    }
    protected Map<String, QName> getNamespaceCache(Namespace namespace) {
        if (namespace == Namespace.NO_NAMESPACE) {
            return noNamespaceCache;
        }
        Map<String, QName> answer = null;
        if (namespace != null) {
            answer = namespaceCache.get(namespace);
        }
        if (answer == null) {
            answer = createMap();
            namespaceCache.put(namespace, answer);
        }
        return answer;
    }
    protected Map<String, QName> createMap() {
        return Collections.synchronizedMap(new HashMap<String, QName>());
    }
    protected QName createQName(String name) {
        return new QName(name);
    }
    protected QName createQName(String name, Namespace namespace) {
        return new QName(name, namespace);
    }
    protected QName createQName(String name, Namespace namespace,
            String qualifiedName) {
        return new QName(name, namespace, qualifiedName);
    }
}
