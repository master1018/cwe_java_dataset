
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_81_bad extends CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_81_base
{
    public void action(int data ) throws Throwable
    {
        HashMap intHashMap = new HashMap(data);
    }
}
