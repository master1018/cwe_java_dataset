
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.HashMap;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = null;
        HashMap<Integer,StringBuilder> dataHashMap = new HashMap<Integer,StringBuilder>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder();
        HashMap<Integer,StringBuilder> dataHashMap = new HashMap<Integer,StringBuilder>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_74b()).goodG2BSink(dataHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = null;
        HashMap<Integer,StringBuilder> dataHashMap = new HashMap<Integer,StringBuilder>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_74b()).goodB2GSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
