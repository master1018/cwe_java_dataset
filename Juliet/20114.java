
package testcases.CWE90_LDAP_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE90_LDAP_Injection__getParameter_Servlet_61b
{
    public String badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        return data;
    }
    public String goodG2BSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        return data;
    }
}
