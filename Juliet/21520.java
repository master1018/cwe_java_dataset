
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashSet_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashSet_68a.data;
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashSet_68a.data;
        HashSet intHashSet = new HashSet(data);
    }
}
