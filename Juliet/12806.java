
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_console_readLine_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count = (new CWE400_Resource_Exhaustion__sleep_console_readLine_61b()).badSource();
        Thread.sleep(count);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count = (new CWE400_Resource_Exhaustion__sleep_console_readLine_61b()).goodG2BSource();
        Thread.sleep(count);
    }
    private void goodB2G() throws Throwable
    {
        int count = (new CWE400_Resource_Exhaustion__sleep_console_readLine_61b()).goodB2GSource();
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
