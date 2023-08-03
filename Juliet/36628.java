
package testcases.CWE511_Logic_Time_Bomb;
import testcasesupport.*;
public class CWE511_Logic_Time_Bomb__counter_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count = 0;
        do
        {
            if (count == 20000)
            {
                Runtime.getRuntime().exec("c:\\windows\\system32\\evil.exe");
            }
            count++;
        }
        while (count < Integer.MAX_VALUE);
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        int count = 0;
        do
        {
            if (count == 20000)
            {
                IO.writeLine("Sorry, your license has expired.  Please contact support.");
            }
            count++;
        }
        while (count < Integer.MAX_VALUE);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
