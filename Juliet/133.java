
package testcases.CWE481_Assigning_Instead_of_Comparing;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE481_Assigning_Instead_of_Comparing__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int zeroOrOne = (new SecureRandom()).nextInt(2);
        boolean isZero = (zeroOrOne == 0);
        if(isZero = true) 
        {
            IO.writeLine("zeroOrOne is 0");
        }
        IO.writeLine("isZero is: " + isZero);
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        int zeroOrOne = (new SecureRandom()).nextInt(2); 
        boolean isZero = (zeroOrOne == 0);
        if(isZero == true) 
        {
            IO.writeLine("zeroOrOne is 0");
        }
        IO.writeLine("isZero is: " + isZero);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
