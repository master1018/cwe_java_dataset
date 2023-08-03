
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_square_81_goodG2B extends CWE190_Integer_Overflow__int_getQueryString_Servlet_square_81_base
{
    public void action(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
}
