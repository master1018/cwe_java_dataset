
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__sleep_Property_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        int count;
        if (privateTrue)
        {
            count = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getProperty("user.home");
                try
                {
                    count = Integer.parseInt(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing count from string", exceptNumberFormat);
                }
            }
        }
        else
        {
            count = 0;
        }
        if (privateTrue)
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        if (privateFalse)
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (privateTrue)
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        if (privateTrue)
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (privateTrue)
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        if (privateTrue)
        {
            count = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getProperty("user.home");
                try
                {
                    count = Integer.parseInt(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing count from string", exceptNumberFormat);
                }
            }
        }
        else
        {
            count = 0;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int count;
        if (privateTrue)
        {
            count = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getProperty("user.home");
                try
                {
                    count = Integer.parseInt(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing count from string", exceptNumberFormat);
                }
            }
        }
        else
        {
            count = 0;
        }
        if (privateTrue)
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
