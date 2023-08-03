
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_ArrayList_52c
{
    public void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        ArrayList intArrayList = new ArrayList(data);
    }
}
