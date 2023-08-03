
package testcases.CWE81_XSS_Error_Message;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE81_XSS_Error_Message__Servlet_URLConnection_81_goodG2B extends CWE81_XSS_Error_Message__Servlet_URLConnection_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            response.sendError(404, "<br>bad() - Parameter name has value " + data);
        }
    }
}
