
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE113_HTTP_Response_Splitting__getQueryString_Servlet_addHeaderServlet_71a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
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
        (new CWE113_HTTP_Response_Splitting__getQueryString_Servlet_addHeaderServlet_71b()).badSink((Object)data , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        (new CWE113_HTTP_Response_Splitting__getQueryString_Servlet_addHeaderServlet_71b()).goodG2BSink((Object)data , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
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
        (new CWE113_HTTP_Response_Splitting__getQueryString_Servlet_addHeaderServlet_71b()).goodB2GSink((Object)data , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}