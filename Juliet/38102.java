
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__sleep_Property_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int count;
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
        return count;
    }
    public void bad() throws Throwable
    {
        int count = badSource();
        Thread.sleep(count);
    }
    private int goodG2BSource() throws Throwable
    {
        int count;
        count = 2;
        return count;
    }
    private void goodG2B() throws Throwable
    {
        int count = goodG2BSource();
        Thread.sleep(count);
    }
    private int goodB2GSource() throws Throwable
    {
        int count;
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
        return count;
    }
    private void goodB2G() throws Throwable
    {
        int count = goodB2GSource();
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
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
