
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.Vector;
public class CWE476_NULL_Pointer_Dereference__Integer_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        Integer data;
        data = null;
        Vector<Integer> dataVector = new Vector<Integer>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__Integer_72b()).badSink(dataVector  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        Integer data;
        data = Integer.valueOf(5);
        Vector<Integer> dataVector = new Vector<Integer>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__Integer_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        Integer data;
        data = null;
        Vector<Integer> dataVector = new Vector<Integer>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__Integer_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
