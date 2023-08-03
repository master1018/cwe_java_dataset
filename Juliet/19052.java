
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE113_HTTP_Response_Splitting__getQueryString_Servlet_setHeaderServlet_66a extends AbstractTestCaseServlet
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE113_HTTP_Response_Splitting__getQueryString_Servlet_setHeaderServlet_66b()).badSink(dataArray , request, response );
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE113_HTTP_Response_Splitting__getQueryString_Servlet_setHeaderServlet_66b()).goodG2BSink(dataArray , request, response );
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE113_HTTP_Response_Splitting__getQueryString_Servlet_setHeaderServlet_66b()).goodB2GSink(dataArray , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
