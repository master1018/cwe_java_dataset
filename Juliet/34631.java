
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashMap_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        while (true)
        {
            data = (new SecureRandom()).nextInt();
            break;
        }
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B() throws Throwable
    {
        int data;
        while (true)
        {
            data = 2;
            break;
        }
        HashMap intHashMap = new HashMap(data);
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
