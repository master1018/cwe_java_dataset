
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.HashMap;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
        HashMap<Integer,Short> dataHashMap = new HashMap<Integer,Short>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE197_Numeric_Truncation_Error__short_random_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        HashMap<Integer,Short> dataHashMap = new HashMap<Integer,Short>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE197_Numeric_Truncation_Error__short_random_74b()).goodG2BSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
