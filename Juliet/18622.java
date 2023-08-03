
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_ArrayList_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        (new CWE789_Uncontrolled_Mem_Alloc__max_value_ArrayList_71b()).badSink((Object)data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        (new CWE789_Uncontrolled_Mem_Alloc__max_value_ArrayList_71b()).goodG2BSink((Object)data  );
    }
    public static void main(String[] args)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
