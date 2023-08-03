
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_61b()).badSource(request, response);
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_61b()).goodG2BSource(request, response);
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_61b()).goodB2GSource(request, response);
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
