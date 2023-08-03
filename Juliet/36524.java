
package testcases.CWE483_Incorrect_Block_Delimitation;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE483_Incorrect_Block_Delimitation__if_without_braces_multiline_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int x, y;
        x = (new SecureRandom()).nextInt(3);
        y = 0;
        if (x == 0)
            IO.writeLine("x == 0");
            y = 1; 
        if (y == 1) 
        {
            IO.writeLine("x was 0");
        }
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
        if (y == 1) 
        {
            IO.writeLine("x was 0");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
