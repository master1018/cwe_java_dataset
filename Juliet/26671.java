
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashMap_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = (new SecureRandom()).nextInt();
            dataCopy = data;
        }
        {
            int data = dataCopy;
            HashMap intHashMap = new HashMap(data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = 2;
            dataCopy = data;
        }
        {
            int data = dataCopy;
            HashMap intHashMap = new HashMap(data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}