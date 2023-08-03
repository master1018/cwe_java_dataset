
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__short_Environment_61b
{
    public short badSource() throws Throwable
    {
        short data;
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
        return data;
    }
    public short goodG2BSource() throws Throwable
    {
        short data;
        data = 2;
        return data;
    }
}
