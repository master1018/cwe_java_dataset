
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_PropertiesFile_71b
{
    public void badSink(Object countObject ) throws Throwable
    {
        int count = (Integer)countObject;
        Thread.sleep(count);
    }
    public void goodG2BSink(Object countObject ) throws Throwable
    {
        int count = (Integer)countObject;
        Thread.sleep(count);
    }
    public void goodB2GSink(Object countObject ) throws Throwable
    {
        int count = (Integer)countObject;
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
