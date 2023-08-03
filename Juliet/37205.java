
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_ArrayList_67b
{
    public void badSink(CWE789_Uncontrolled_Mem_Alloc__Environment_ArrayList_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(CWE789_Uncontrolled_Mem_Alloc__Environment_ArrayList_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        ArrayList intArrayList = new ArrayList(data);
    }
}
