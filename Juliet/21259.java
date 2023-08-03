
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__int_Environment_to_short_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getenv("ADD");
                if (stringNumber != null) 
                {
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
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FALSE)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((short)data);
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
