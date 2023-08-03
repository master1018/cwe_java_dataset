package org.jolokia.restrictor;
import javax.management.ObjectName;
import org.jolokia.util.HttpMethod;
import org.jolokia.util.RequestType;
public interface Restrictor {
    boolean isHttpMethodAllowed(HttpMethod pMethod);
    boolean isTypeAllowed(RequestType pType);
    boolean isAttributeReadAllowed(ObjectName pName,String pAttribute);
    boolean isAttributeWriteAllowed(ObjectName pName,String pAttribute);
    boolean isOperationAllowed(ObjectName pName,String pOperation);
    boolean isRemoteAccessAllowed(String ... pHostOrAddress);
    boolean isCorsAccessAllowed(String pOrigin);
}
