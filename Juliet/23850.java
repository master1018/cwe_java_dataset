
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
import java.util.HashMap;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Short.MAX_VALUE + 5;
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE197_Numeric_Truncation_Error__int_large_to_short_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE197_Numeric_Truncation_Error__int_large_to_short_74b()).goodG2BSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
