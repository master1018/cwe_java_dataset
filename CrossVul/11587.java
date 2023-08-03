
package org.jboss.weld.tests.contexts.cache;
import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
@ConversationScoped
public class ConversationScopedBean implements Serializable {
    private String value = "foo";
    public String getAndSet(String newValue) {
        String old = value;
        value = newValue;
        return old;
    }
}
