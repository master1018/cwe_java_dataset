
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_getCookies_Servlet_divide_81_goodB2G extends CWE369_Divide_by_Zero__int_getCookies_Servlet_divide_81_base
{
    public void action(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
}