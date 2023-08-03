
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_HashSet_51b
{
    public void badSink(int data ) throws Throwable
    {
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        HashSet intHashSet = new HashSet(data);
    }
}
