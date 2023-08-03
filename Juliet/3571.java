
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.util.Vector;
public class CWE400_Resource_Exhaustion__sleep_random_72b
{
    public void badSink(Vector<Integer> countVector ) throws Throwable
    {
        int count = countVector.remove(2);
        Thread.sleep(count);
    }
    public void goodG2BSink(Vector<Integer> countVector ) throws Throwable
    {
        int count = countVector.remove(2);
        Thread.sleep(count);
    }
    public void goodB2GSink(Vector<Integer> countVector ) throws Throwable
    {
        int count = countVector.remove(2);
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
