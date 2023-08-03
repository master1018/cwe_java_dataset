
package testcases.CWE482_Comparing_Instead_of_Assigning;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE482_Comparing_Instead_of_Assigning__basic_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (true)
        {
            int zeroOrOne = (new SecureRandom()).nextInt(2);
            boolean isZero = false;
            if((isZero == (zeroOrOne == 0)) == true) 
            {
                IO.writeLine("zeroOrOne is 0");
            }
            IO.writeLine("isZero is: " + isZero);
        }
    }
    private void good1() throws Throwable
    {
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int zeroOrOne = (new SecureRandom()).nextInt(2);
            boolean isZero = false;
            if((isZero = (zeroOrOne == 0)) == true) 
            {
                IO.writeLine("zeroOrOne is 0");
            }
            IO.writeLine("isZero is: " + isZero);
        }
    }
    private void good2() throws Throwable
    {
        if (true)
        {
            int zeroOrOne = (new SecureRandom()).nextInt(2);
            boolean isZero = false;
            if((isZero = (zeroOrOne == 0)) == true) 
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
