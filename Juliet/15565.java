
package testcases.CWE83_XSS_Attribute;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE83_XSS_Attribute__Servlet_PropertiesFile_72b
{
    public void badSink(Vector<String> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
    public void goodG2BSink(Vector<String> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
}
