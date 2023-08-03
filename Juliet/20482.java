
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.Vector;
public class CWE476_NULL_Pointer_Dereference__String_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = null;
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__String_72b()).badSink(dataVector  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "This is not null";
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__String_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = null;
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__String_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
