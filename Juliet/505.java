
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__console_readLine_for_loop_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count = (new CWE400_Resource_Exhaustion__console_readLine_for_loop_61b()).badSource();
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count = (new CWE400_Resource_Exhaustion__console_readLine_for_loop_61b()).goodG2BSource();
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    private void goodB2G() throws Throwable
    {
        int count = (new CWE400_Resource_Exhaustion__console_readLine_for_loop_61b()).goodB2GSource();
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
