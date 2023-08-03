
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.HashMap;
public class CWE400_Resource_Exhaustion__sleep_listen_tcp_74b
{
    public void badSink(HashMap<Integer,Integer> countHashMap ) throws Throwable
    {
        int count = countHashMap.get(2);
        Thread.sleep(count);
    }
    public void goodG2BSink(HashMap<Integer,Integer> countHashMap ) throws Throwable
    {
        int count = countHashMap.get(2);
        Thread.sleep(count);
    }
    public void goodB2GSink(HashMap<Integer,Integer> countHashMap ) throws Throwable
    {
        int count = countHashMap.get(2);
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
