
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_URLConnection_68b
{
    public void badSink() throws Throwable
    {
        int count = CWE400_Resource_Exhaustion__sleep_URLConnection_68a.count;
        Thread.sleep(count);
    }
    public void goodG2BSink() throws Throwable
    {
        int count = CWE400_Resource_Exhaustion__sleep_URLConnection_68a.count;
        Thread.sleep(count);
    }
    public void goodB2GSink() throws Throwable
    {
        int count = CWE400_Resource_Exhaustion__sleep_URLConnection_68a.count;
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
