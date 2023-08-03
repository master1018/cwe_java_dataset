
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__short_Environment_68a extends AbstractTestCase
{
    public static short data;
    public void bad() throws Throwable
    {
        data = Short.MIN_VALUE; 
        {
            String stringNumber = System.getenv("ADD");
            if (stringNumber != null) 
            {
                try
                {
                    data = Short.parseShort(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        (new CWE197_Numeric_Truncation_Error__short_Environment_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE197_Numeric_Truncation_Error__short_Environment_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
