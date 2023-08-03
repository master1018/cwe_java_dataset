
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_multiply_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        HashMap<Integer,Long> dataHashMap = new HashMap<Integer,Long>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE190_Integer_Overflow__long_max_multiply_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        HashMap<Integer,Long> dataHashMap = new HashMap<Integer,Long>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE190_Integer_Overflow__long_max_multiply_74b()).goodG2BSink(dataHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        HashMap<Integer,Long> dataHashMap = new HashMap<Integer,Long>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE190_Integer_Overflow__long_max_multiply_74b()).goodB2GSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
