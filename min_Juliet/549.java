
package testcases.CWE511_Logic_Time_Bomb;
import testcasesupport.*;
public class CWE511_Logic_Time_Bomb__counter_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        if (privateTrue)
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
    }
    private void good1() throws Throwable
    {
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
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
    }
    private void good2() throws Throwable
    {
        if (privateTrue)
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
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
