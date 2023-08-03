
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__float_Property_divide_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        switch (6)
        {
        case 6:
            data = -1.0f; 
            {
                String stringNumber = System.getProperty("user.home");
                if (stringNumber != null)
                {
                    try
                    {
                        data = Float.parseFloat(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
            }
            break;
        default:
            data = 0.0f;
            break;
        }
        switch (7)
        {
        case 7:
            int result = (int)(100.0 / data);
            IO.writeLine(result);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        float data;
        switch (5)
        {
        case 6:
            data = 0.0f;
            break;
        default:
            data = 2.0f;
            break;
        }
        switch (7)
        {
        case 7:
            int result = (int)(100.0 / data);
            IO.writeLine(result);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        float data;
        switch (6)
        {
        case 6:
            data = 2.0f;
            break;
        default:
            data = 0.0f;
            break;
        }
        switch (7)
        {
        case 7:
            int result = (int)(100.0 / data);
            IO.writeLine(result);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        float data;
        switch (6)
        {
        case 6:
            data = -1.0f; 
            {
                String stringNumber = System.getProperty("user.home");
                if (stringNumber != null)
                {
                    try
                    {
                        data = Float.parseFloat(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
            }
            break;
        default:
            data = 0.0f;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 / data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
            }
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        float data;
        switch (6)
        {
        case 6:
            data = -1.0f; 
            {
                String stringNumber = System.getProperty("user.home");
                if (stringNumber != null)
                {
                    try
                    {
                        data = Float.parseFloat(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
            }
            break;
        default:
            data = 0.0f;
            break;
        }
        switch (7)
        {
        case 7:
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 / data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
