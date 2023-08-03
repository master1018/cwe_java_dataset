
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__database_ArrayList_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        ArrayList intArrayList = new ArrayList(data);
    }
}
