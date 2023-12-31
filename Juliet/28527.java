
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_square_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE190_Integer_Overflow__int_getQueryString_Servlet_square_61b()).badSource(request, response);
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE190_Integer_Overflow__int_getQueryString_Servlet_square_61b()).goodG2BSource(request, response);
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE190_Integer_Overflow__int_getQueryString_Servlet_square_61b()).goodB2GSource(request, response);
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
