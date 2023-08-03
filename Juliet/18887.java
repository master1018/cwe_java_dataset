
package testcases.CWE209_Information_Leak_Error;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE209_Information_Leak_Error__printStackTrace_Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        try
        {
            throw new UnsupportedOperationException();
        }
        catch (UnsupportedOperationException exceptUnsupportedOperation)
        {
            exceptUnsupportedOperation.printStackTrace(response.getWriter()); 
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        try
        {
            throw new UnsupportedOperationException();
        }
        catch (UnsupportedOperationException exceptUnsupportedOperation)
        {
            response.getWriter().println("There was an unsupported operation error"); 
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
