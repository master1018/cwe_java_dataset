
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashSet_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        for (int i = 0; i < 1; i++)
        {
            HashSet intHashSet = new HashSet(data);
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        for (int i = 0; i < 1; i++)
        {
            HashSet intHashSet = new HashSet(data);
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
