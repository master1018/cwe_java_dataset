
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_68a.data;
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_68a.data;
        HashMap intHashMap = new HashMap(data);
    }
}
