
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_getParameter_Servlet_multiply_81_bad extends CWE191_Integer_Underflow__int_getParameter_Servlet_multiply_81_base
{
    public void action(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
