
package testcases.CWE483_Incorrect_Block_Delimitation;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE483_Incorrect_Block_Delimitation__semicolon_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (5 == 5)
        {
            int x, y;
            x = (new SecureRandom()).nextInt(3);
            y = 0;
            if (x == 0);
            {
                IO.writeLine("x == 0");
                y = 1; 
            }
            IO.writeLine(y);
        }
    }
    private void good1() throws Throwable
    {
        if (5 != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int x, y;
            x = (new SecureRandom()).nextInt(3);
            y = 0;
            if (x == 0)
            {
                IO.writeLine("x == 0");
                y = 1; 
            }
            IO.writeLine(y);
        }
    }
    private void good2() throws Throwable
    {
        if (5 == 5)
        {
            int x, y;
            x = (new SecureRandom()).nextInt(3);
            y = 0;
            if (x == 0)
            {
                IO.writeLine("x == 0");
                y = 1; 
            }
            IO.writeLine(y);
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
