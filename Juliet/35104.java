
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__sleep_Environment_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        while (true)
        {
            count = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getenv("ADD");
                if (stringNumber != null) 
                {
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
            break;
        }
        while (true)
        {
            Thread.sleep(count);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        int count;
        while (true)
        {
            count = 2;
            break;
        }
        while (true)
        {
            Thread.sleep(count);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        int count;
        while (true)
        {
            count = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getenv("ADD");
                if (stringNumber != null) 
                {
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
            break;
        }
        while (true)
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
