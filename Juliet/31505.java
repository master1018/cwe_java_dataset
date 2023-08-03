
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__File_ArrayList_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__File_ArrayList_68a.data;
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE789_Uncontrolled_Mem_Alloc__File_ArrayList_68a.data;
        ArrayList intArrayList = new ArrayList(data);
    }
}
