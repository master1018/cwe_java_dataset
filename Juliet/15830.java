
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashSet_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashSet_68a.data;
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashSet_68a.data;
        HashSet intHashSet = new HashSet(data);
    }
}
