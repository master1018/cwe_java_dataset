package org.jolokia.restrictor;
import java.io.IOException;
import java.io.InputStream;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jolokia.restrictor.policy.*;
import org.jolokia.util.HttpMethod;
import org.jolokia.util.RequestType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
public class PolicyRestrictor implements Restrictor {
    private HttpMethodChecker httpChecker;
    private RequestTypeChecker requestTypeChecker;
    private NetworkChecker networkChecker;
    private CorsChecker corsChecker;
    private MBeanAccessChecker mbeanAccessChecker;
    public PolicyRestrictor(InputStream pInput) {
        Exception exp = null;
        if (pInput == null) {
            throw new SecurityException("No policy file given");
        }
        try {
            Document doc =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pInput);
            requestTypeChecker = new RequestTypeChecker(doc);
            httpChecker = new HttpMethodChecker(doc);
            networkChecker = new NetworkChecker(doc);
            mbeanAccessChecker = new MBeanAccessChecker(doc);
            corsChecker = new CorsChecker(doc);
        }
        catch (SAXException e) { exp = e; }
        catch (IOException e) { exp = e; }
        catch (ParserConfigurationException e) { exp = e; }
        catch (MalformedObjectNameException e) { exp = e; }
        if (exp != null) {
            throw new SecurityException("Cannot parse policy file: " + exp,exp);
        }
    }
    public boolean isHttpMethodAllowed(HttpMethod method) {
        return httpChecker.check(method);
    }
    public boolean isTypeAllowed(RequestType pType) {
        return requestTypeChecker.check(pType);
    }
    public boolean isRemoteAccessAllowed(String ... pHostOrAddress) {
        return networkChecker.check(pHostOrAddress);
    }
    public boolean isCorsAccessAllowed(String pOrigin) {
        return corsChecker.check(pOrigin);
    }
    public boolean isAttributeReadAllowed(ObjectName pName, String pAttribute) {
        return check(RequestType.READ,pName,pAttribute);
    }
    public boolean isAttributeWriteAllowed(ObjectName pName, String pAttribute) {
        return check(RequestType.WRITE,pName, pAttribute);
    }
    public boolean isOperationAllowed(ObjectName pName, String pOperation) {
        return check(RequestType.EXEC,pName, pOperation);
    }
    private boolean check(RequestType pType, ObjectName pName, String pValue) {
        return mbeanAccessChecker.check(new MBeanAccessChecker.Arg(isTypeAllowed(pType), pType, pName, pValue));
    }
}
