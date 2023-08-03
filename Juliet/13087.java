
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE113_HTTP_Response_Splitting__Environment_addCookieServlet_61b
{
    public String badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        return data;
    }
    public String goodG2BSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        return data;
    }
    public String goodB2GSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        return data;
    }
}
