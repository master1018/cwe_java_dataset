
package testcases.CWE511_Logic_Time_Bomb;
import testcasesupport.*;
public class CWE511_Logic_Time_Bomb__counter_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
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
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
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
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
