
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_61b()).badSource(request, response);
        IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_61b()).goodG2BSource(request, response);
        IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_61b()).goodB2GSource(request, response);
        if (data != 0)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a modulo by zero");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
