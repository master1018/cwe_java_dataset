
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        ArrayList intArrayList = new ArrayList(data);
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
