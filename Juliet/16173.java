
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_67b
{
    public void badSink(CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashMap intHashMap = new HashMap(data);
    }
}
