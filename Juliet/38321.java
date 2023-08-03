
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__short_Property_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short dataCopy;
        {
            short data;
            data = Short.MIN_VALUE; 
            {
                String stringNumber = System.getProperty("user.home");
                try
                {
                    data = Short.parseShort(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
            dataCopy = data;
        }
        {
            short data = dataCopy;
            {
                IO.writeLine((byte)data);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short dataCopy;
        {
            short data;
            data = 2;
            dataCopy = data;
        }
        {
            short data = dataCopy;
            {
                IO.writeLine((byte)data);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
