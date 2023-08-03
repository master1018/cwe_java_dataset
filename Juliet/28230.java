
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__File_HashSet_81_goodG2B extends CWE789_Uncontrolled_Mem_Alloc__File_HashSet_81_base
{
    public void action(int data ) throws Throwable
    {
        HashSet intHashSet = new HashSet(data);
    }
}
