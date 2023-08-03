
package testcases.CWE483_Incorrect_Block_Delimitation;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE483_Incorrect_Block_Delimitation__semicolon_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
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
        for(int k = 0; k < 1; k++)
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
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
