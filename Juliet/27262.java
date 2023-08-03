
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__sleep_Environment_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
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
        int[] countArray = new int[5];
        countArray[2] = count;
        (new CWE400_Resource_Exhaustion__sleep_Environment_66b()).badSink(countArray  );
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
        int[] countArray = new int[5];
        countArray[2] = count;
        (new CWE400_Resource_Exhaustion__sleep_Environment_66b()).goodG2BSink(countArray  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
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
        int[] countArray = new int[5];
        countArray[2] = count;
        (new CWE400_Resource_Exhaustion__sleep_Environment_66b()).goodB2GSink(countArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
