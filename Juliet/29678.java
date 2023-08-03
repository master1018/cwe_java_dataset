
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = Integer.MAX_VALUE;
        (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
