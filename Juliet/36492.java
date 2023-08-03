
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_81_goodB2G extends CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_81_base
{
    public void action(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
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
}
