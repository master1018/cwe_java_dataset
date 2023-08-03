
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_66b
{
    public void badSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(int dataArray[] ) throws Throwable
    {
        int data = dataArray[2];
        HashSet intHashSet = new HashSet(data);
    }
}
