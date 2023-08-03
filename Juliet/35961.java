
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__File_HashSet_67b
{
    public void badSink(CWE789_Uncontrolled_Mem_Alloc__File_HashSet_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(CWE789_Uncontrolled_Mem_Alloc__File_HashSet_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashSet intHashSet = new HashSet(data);
    }
}
