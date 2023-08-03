
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__random_for_loop_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        HashMap<Integer,Integer> countHashMap = new HashMap<Integer,Integer>();
        countHashMap.put(0, count);
        countHashMap.put(1, count);
        countHashMap.put(2, count);
        (new CWE400_Resource_Exhaustion__random_for_loop_74b()).badSink(countHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        HashMap<Integer,Integer> countHashMap = new HashMap<Integer,Integer>();
        countHashMap.put(0, count);
        countHashMap.put(1, count);
        countHashMap.put(2, count);
        (new CWE400_Resource_Exhaustion__random_for_loop_74b()).goodG2BSink(countHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        HashMap<Integer,Integer> countHashMap = new HashMap<Integer,Integer>();
        countHashMap.put(0, count);
        countHashMap.put(1, count);
        countHashMap.put(2, count);
        (new CWE400_Resource_Exhaustion__random_for_loop_74b()).goodB2GSink(countHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
