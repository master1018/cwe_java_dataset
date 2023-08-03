
package testcases.CWE481_Assigning_Instead_of_Comparing;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE481_Assigning_Instead_of_Comparing__basic_08 extends AbstractTestCase
{
    private boolean privateReturnsTrue()
    {
        return true;
    }
    private boolean privateReturnsFalse()
    {
        return false;
    }
    public void bad() throws Throwable
    {
        if (privateReturnsTrue())
        {
            int zeroOrOne = (new SecureRandom()).nextInt(2);
            boolean isZero = (zeroOrOne == 0);
            if(isZero = true) 
            {
                IO.writeLine("zeroOrOne is 0");
            }
            IO.writeLine("isZero is: " + isZero);
        }
    }
    private void good1() throws Throwable
    {
        if (privateReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int zeroOrOne = (new SecureRandom()).nextInt(2); 
            boolean isZero = (zeroOrOne == 0);
            if(isZero == true) 
            {
                IO.writeLine("zeroOrOne is 0");
            }
            IO.writeLine("isZero is: " + isZero);
        }
    }
    private void good2() throws Throwable
    {
        if (privateReturnsTrue())
        {
            int zeroOrOne = (new SecureRandom()).nextInt(2); 
            boolean isZero = (zeroOrOne == 0);
            if(isZero == true) 
            {
                IO.writeLine("zeroOrOne is 0");
            }
            IO.writeLine("isZero is: " + isZero);
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
