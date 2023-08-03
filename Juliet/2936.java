
package testcases.CWE113_HTTP_Response_Splitting.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__URLConnection_addCookieServlet_81_goodG2B extends CWE113_HTTP_Response_Splitting__URLConnection_addCookieServlet_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
}
