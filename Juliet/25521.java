
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_getCookies_Servlet_21 extends AbstractTestCaseServlet
{
    private boolean badPrivate = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        badPrivate = true;
        data = bad_source(request, response);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    private String bad_source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (badPrivate)
        {
            data = ""; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    data = cookieSources[0].getValue();
                }
            }
        }
        else
        {
            data = null;
        }
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B1_private = false;
        data = goodG2B1_source(request, response);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    private String goodG2B1_source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        if (goodG2B1_private)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        return data;
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B2_private = true;
        data = goodG2B2_source(request, response);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    private String goodG2B2_source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        if (goodG2B2_private)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
