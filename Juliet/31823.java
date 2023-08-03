
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_61b()).badSource(request, response);
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_61b()).goodG2BSource(request, response);
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_61b()).goodB2GSource(request, response);
        if (data > Integer.MIN_VALUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
