
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_File_66b
{
    public void badSink(int countArray[] ) throws Throwable
    {
        int count = countArray[2];
        Thread.sleep(count);
    }
    public void goodG2BSink(int countArray[] ) throws Throwable
    {
        int count = countArray[2];
        Thread.sleep(count);
    }
    public void goodB2GSink(int countArray[] ) throws Throwable
    {
        int count = countArray[2];
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
