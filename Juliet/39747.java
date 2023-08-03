
package testcases.CWE83_XSS_Attribute;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE83_XSS_Attribute__Servlet_database_73b
{
    public void badSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
    public void goodG2BSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad() - <img src=\"" + data + "\">");
        }
    }
}
