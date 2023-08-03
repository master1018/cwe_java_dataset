
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (true)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        HashSet intHashSet = new HashSet(data);
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (false)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        HashSet intHashSet = new HashSet(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (true)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        HashSet intHashSet = new HashSet(data);
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
