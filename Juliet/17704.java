
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import java.util.HashMap;
public class CWE191_Integer_Underflow__byte_min_multiply_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        HashMap<Integer,Byte> dataHashMap = new HashMap<Integer,Byte>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE191_Integer_Underflow__byte_min_multiply_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        HashMap<Integer,Byte> dataHashMap = new HashMap<Integer,Byte>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE191_Integer_Underflow__byte_min_multiply_74b()).goodG2BSink(dataHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        HashMap<Integer,Byte> dataHashMap = new HashMap<Integer,Byte>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE191_Integer_Underflow__byte_min_multiply_74b()).goodB2GSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
