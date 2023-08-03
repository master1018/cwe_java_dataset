
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__getCookies_Servlet_addHeaderServlet_15 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = ""; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    data = cookieSources[0].getValue();
                }
            }
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if (data != null)
            {
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "foo";
            break;
        }
        switch (7)
        {
        case 7:
            if (data != null)
            {
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "foo";
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if (data != null)
            {
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = ""; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    data = cookieSources[0].getValue();
                }
            }
            break;
        default:
            data = null;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            if (data != null)
            {
                data = URLEncoder.encode(data, "UTF-8");
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
            break;
        }
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = ""; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    data = cookieSources[0].getValue();
                }
            }
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if (data != null)
            {
                data = URLEncoder.encode(data, "UTF-8");
                response.addHeader("Location", "/author.jsp?lang=" + data);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
        goodB2G1(request, response);
        goodB2G2(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
