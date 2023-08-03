
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_ArrayList_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        for (int i = 0; i < 1; i++)
        {
            ArrayList intArrayList = new ArrayList(data);
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        for (int i = 0; i < 1; i++)
        {
            ArrayList intArrayList = new ArrayList(data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
