
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__random_for_loop_81_goodG2B extends CWE400_Resource_Exhaustion__random_for_loop_81_base
{
    public void action(int count ) throws Throwable
    {
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
}
