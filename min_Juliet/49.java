
package testcases.CWE81_XSS_Error_Message;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE81_XSS_Error_Message__Servlet_getParameter_Servlet_66b
{
    public void badSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            response.sendError(404, "<br>bad() - Parameter name has value " + data);
        }
    }
    public void goodG2BSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            response.sendError(404, "<br>bad() - Parameter name has value " + data);
        }
    }
}
