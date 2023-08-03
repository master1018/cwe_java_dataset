
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.util.HashMap;
public class CWE369_Divide_by_Zero__float_zero_modulo_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        HashMap<Integer,Float> dataHashMap = new HashMap<Integer,Float>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE369_Divide_by_Zero__float_zero_modulo_74b()).badSink(dataHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        HashMap<Integer,Float> dataHashMap = new HashMap<Integer,Float>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE369_Divide_by_Zero__float_zero_modulo_74b()).goodG2BSink(dataHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        data = 0.0f; 
        HashMap<Integer,Float> dataHashMap = new HashMap<Integer,Float>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE369_Divide_by_Zero__float_zero_modulo_74b()).goodB2GSink(dataHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
