
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_67b
{
    public void badSink(CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashMap intHashMap = new HashMap(data);
    }
}
