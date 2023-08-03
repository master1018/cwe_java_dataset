
package testcases.CWE483_Incorrect_Block_Delimitation;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE483_Incorrect_Block_Delimitation__semicolon_01 extends AbstractTestCase
{
    public void bad() throws Throwable
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
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
