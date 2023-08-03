package org.jolokia.restrictor;
import javax.management.ObjectName;
import org.jolokia.util.HttpMethod;
import org.jolokia.util.RequestType;
public abstract class AbstractConstantRestrictor implements Restrictor {
    private boolean isAllowed;
    protected AbstractConstantRestrictor(boolean pAllowed) {
        isAllowed = pAllowed;
    }
    public final boolean isHttpMethodAllowed(HttpMethod pMethod) {
        return isAllowed;
    }
    public final boolean isTypeAllowed(RequestType pType) {
        return isAllowed;
    }
    public final boolean isAttributeReadAllowed(ObjectName pName, String pAttribute) {
        return isAllowed;
    }
    public final boolean isAttributeWriteAllowed(ObjectName pName, String pAttribute) {
        return isAllowed;
    }
    public final boolean isOperationAllowed(ObjectName pName, String pOperation) {
        return isAllowed;
    }
    public final boolean isRemoteAccessAllowed(String... pHostOrAddress) {
        return isAllowed;
    }
    public boolean isCorsAccessAllowed(String pOrigin) {
        return isAllowed;
    }
}
