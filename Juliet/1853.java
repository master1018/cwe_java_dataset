
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.HashMap;
public class CWE476_NULL_Pointer_Dereference__int_array_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int [] data;
        data = null;
        HashMap<Integer,int []> dataHashMap = new HashMap<Integer,int []>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE476_NULL_Pointer_Dereference__int_array_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int [] data;
        data = new int[5];
        HashMap<Integer,int []> dataHashMap = new HashMap<Integer,int []>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE476_NULL_Pointer_Dereference__int_array_74b()).goodG2BSink(dataHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        int [] data;
        data = null;
        HashMap<Integer,int []> dataHashMap = new HashMap<Integer,int []>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE476_NULL_Pointer_Dereference__int_array_74b()).goodB2GSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
