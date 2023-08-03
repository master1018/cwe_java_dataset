
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_getCookies_Servlet_74b
{
    public void badSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    public void goodG2BSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
