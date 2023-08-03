
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__getParameter_Servlet_22b
{
    public String badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__getParameter_Servlet_22a.badPublicStatic)
        {
            data = request.getParameter("name");
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
        if (CWE470_Unsafe_Reflection__getParameter_Servlet_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "Testing.test";
        }
        return data;
    }
    public String goodG2B2Source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__getParameter_Servlet_22a.goodG2B2PublicStatic)
        {
            data = "Testing.test";
        }
        else
        {
            data = null;
        }
        return data;
    }
}
