
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashMap_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__random_HashMap_68a.data;
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__random_HashMap_68a.data;
        HashMap intHashMap = new HashMap(data);
    }
}
