
package testcases.CWE89_SQL_Injection.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE89_SQL_Injection__getQueryString_Servlet_prepareStatement_68a extends AbstractTestCaseServlet
{
    public static String data;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        (new CWE89_SQL_Injection__getQueryString_Servlet_prepareStatement_68b()).badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = "foo";
        (new CWE89_SQL_Injection__getQueryString_Servlet_prepareStatement_68b()).goodG2BSink(request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        (new CWE89_SQL_Injection__getQueryString_Servlet_prepareStatement_68b()).goodB2GSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}