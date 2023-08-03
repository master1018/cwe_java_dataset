
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__database_for_loop_68b
{
    public void badSink() throws Throwable
    {
        int count = CWE400_Resource_Exhaustion__database_for_loop_68a.count;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodG2BSink() throws Throwable
    {
        int count = CWE400_Resource_Exhaustion__database_for_loop_68a.count;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodB2GSink() throws Throwable
    {
        int count = CWE400_Resource_Exhaustion__database_for_loop_68a.count;
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
}
