
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_Property_67b
{
    public void badSink(CWE400_Resource_Exhaustion__sleep_Property_67a.Container countContainer ) throws Throwable
    {
        int count = countContainer.containerOne;
        Thread.sleep(count);
    }
    public void goodG2BSink(CWE400_Resource_Exhaustion__sleep_Property_67a.Container countContainer ) throws Throwable
    {
        int count = countContainer.containerOne;
        Thread.sleep(count);
    }
    public void goodB2GSink(CWE400_Resource_Exhaustion__sleep_Property_67a.Container countContainer ) throws Throwable
    {
        int count = countContainer.containerOne;
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
