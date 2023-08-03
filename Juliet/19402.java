
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_22a extends AbstractTestCaseServlet
{
    public static boolean badPublicStatic = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        badPublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_22b()).badSource(request, response);
        HashMap intHashMap = new HashMap(data);
    }
    public static boolean goodG2B1PublicStatic = false;
    public static boolean goodG2B2PublicStatic = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        goodG2B1PublicStatic = false;
        data = (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_22b()).goodG2B1Source(request, response);
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        goodG2B2PublicStatic = true;
        data = (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_22b()).goodG2B2Source(request, response);
        HashMap intHashMap = new HashMap(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
