
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE129_Improper_Validation_of_Array_Index__Property_array_write_no_check_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticFive==5)
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
        if (IO.staticFive==5)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            array[data] = 42;
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.staticFive!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.staticFive==5)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            array[data] = 42;
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.staticFive==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.staticFive==5)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            array[data] = 42;
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (IO.staticFive==5)
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
        if (IO.staticFive!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int array[] = { 0, 1, 2, 3, 4 };
            if (data >= 0 && data < array.length)
            {
                array[data] = 42;
            }
            else
            {
                IO.writeLine("Array index out of bounds");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        if (IO.staticFive==5)
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
        if (IO.staticFive==5)
        {
            int array[] = { 0, 1, 2, 3, 4 };
            if (data >= 0 && data < array.length)
            {
                array[data] = 42;
            }
            else
            {
                IO.writeLine("Array index out of bounds");
            }
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
