
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashSet_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        int data;
        if (privateFive == 5)
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
        if (privateFive != 5)
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
        if (privateFive == 5)
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
