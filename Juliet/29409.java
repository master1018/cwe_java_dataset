
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getCookies_Servlet_square_81_bad extends CWE190_Integer_Overflow__int_getCookies_Servlet_square_81_base
{
    public void action(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
}
