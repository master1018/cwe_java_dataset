
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_HashSet_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        HashSet intHashSet = new HashSet(data);
    }
}
