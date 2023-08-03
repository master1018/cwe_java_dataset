
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_61a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_61b()).badSource(request, response);
        HashMap intHashMap = new HashMap(data);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_61b()).goodG2BSource(request, response);
        HashMap intHashMap = new HashMap(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
