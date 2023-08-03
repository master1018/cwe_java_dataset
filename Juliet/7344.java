
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_HashSet_81_bad extends CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_HashSet_81_base
{
    public void action(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        HashSet intHashSet = new HashSet(data);
    }
}
