
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_81_base baseObject = new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_81_base baseObject = new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_81_goodG2B();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
