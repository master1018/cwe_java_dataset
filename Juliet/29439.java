
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__sleep_Property_71a extends AbstractTestCase
{
    public void bad() throws Throwable
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
        (new CWE400_Resource_Exhaustion__sleep_Property_71b()).badSink((Object)count  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        (new CWE400_Resource_Exhaustion__sleep_Property_71b()).goodG2BSink((Object)count  );
    }
    private void goodB2G() throws Throwable
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
        (new CWE400_Resource_Exhaustion__sleep_Property_71b()).goodB2GSink((Object)count  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
