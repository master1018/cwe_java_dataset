
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 100;
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_74b()).goodG2BSink(dataHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 100;
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_size_74b()).goodB2GSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
