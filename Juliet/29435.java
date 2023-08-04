
package testcases.CWE81_XSS_Error_Message;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE81_XSS_Error_Message__Servlet_getQueryString_Servlet_22b
{
    public String badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (CWE81_XSS_Error_Message__Servlet_getQueryString_Servlet_22a.badPublicStatic)
        {
            data = ""; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        data = token.substring(3); 
                        break; 
                    }
                }
            }
        }
        else
        {
            data = null;
        }
        return data;
    }
    public String goodG2B1Source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (CWE81_XSS_Error_Message__Servlet_getQueryString_Servlet_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        return data;
    }
    public String goodG2B2Source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (CWE81_XSS_Error_Message__Servlet_getQueryString_Servlet_22a.goodG2B2PublicStatic)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        return data;
    }
}