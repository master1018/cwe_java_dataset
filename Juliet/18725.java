
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashMap_81_goodG2B extends CWE789_Uncontrolled_Mem_Alloc__random_HashMap_81_base
{
    public void action(int data ) throws Throwable
    {
        HashMap intHashMap = new HashMap(data);
    }
}
