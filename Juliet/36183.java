
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__int_Property_to_byte_06 extends AbstractTestCase
{
    private static final int PRIVATE_STATIC_FINAL_FIVE = 5;
    public void bad() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
            data = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getProperty("user.home");
                try
                {
                    data = Integer.parseInt(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE != 5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((byte)data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
