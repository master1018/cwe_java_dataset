
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE129_Improper_Validation_of_Array_Index__Environment_array_size_09 extends AbstractTestCase
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
        if (IO.STATIC_FINAL_TRUE)
        {
            int array[] = null;
            if (data >= 0)
            {
                array = new int[data];
            }
            else
            {
                IO.writeLine("Array size is negative");
            }
            array[0] = 5;
            IO.writeLine(array[0]);
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
        if (IO.STATIC_FINAL_TRUE)
        {
            int array[] = null;
            if (data >= 0)
            {
                array = new int[data];
            }
            else
            {
                IO.writeLine("Array size is negative");
            }
            array[0] = 5;
            IO.writeLine(array[0]);
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
        if (IO.STATIC_FINAL_TRUE)
        {
            int array[] = null;
            if (data >= 0)
            {
                array = new int[data];
            }
            else
            {
                IO.writeLine("Array size is negative");
            }
            array[0] = 5;
            IO.writeLine(array[0]);
        }
    }
    private void goodB2G1() throws Throwable
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
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int array[] = null;
            if (data > 0)
            {
                array = new int[data];
            }
            else
            {
                IO.writeLine("Array size is negative");
            }
            array[0] = 5;
            IO.writeLine(array[0]);
        }
    }
    private void goodB2G2() throws Throwable
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
        if (IO.STATIC_FINAL_TRUE)
        {
            int array[] = null;
            if (data > 0)
            {
                array = new int[data];
            }
            else
            {
                IO.writeLine("Array size is negative");
            }
            array[0] = 5;
            IO.writeLine(array[0]);
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
