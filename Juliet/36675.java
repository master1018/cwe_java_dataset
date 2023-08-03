
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__database_ArrayList_81_bad extends CWE789_Uncontrolled_Mem_Alloc__database_ArrayList_81_base
{
    public void action(int data ) throws Throwable
    {
        ArrayList intArrayList = new ArrayList(data);
    }
}
