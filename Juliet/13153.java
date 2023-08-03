
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__Property_for_loop_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        switch (6)
        {
        case 6:
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
            break;
        default:
            count = 0;
            break;
        }
        switch (7)
        {
        case 7:
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        switch (5)
        {
        case 6:
            count = 0;
            break;
        default:
            count = 2;
            break;
        }
        switch (7)
        {
        case 7:
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        switch (6)
        {
        case 6:
            count = 2;
            break;
        default:
            count = 0;
            break;
        }
        switch (7)
        {
        case 7:
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        switch (6)
        {
        case 6:
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
            break;
        default:
            count = 0;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
            }
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        int count;
        switch (6)
        {
        case 6:
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
            break;
        default:
            count = 0;
            break;
        }
        switch (7)
        {
        case 7:
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
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
