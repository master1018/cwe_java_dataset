
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__getParameter_Servlet_array_size_22b
{
    public void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__getParameter_Servlet_array_size_22a.badPublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__getParameter_Servlet_array_size_22a.goodB2G1PublicStatic)
        {
            data = 0;
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
    public void goodB2G2Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__getParameter_Servlet_array_size_22a.goodB2G2PublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE129_Improper_Validation_of_Array_Index__getParameter_Servlet_array_size_22a.goodG2BPublicStatic)
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
        else
        {
            data = 0;
        }
    }
}
