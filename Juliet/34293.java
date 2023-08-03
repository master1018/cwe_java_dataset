
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashMap_41 extends AbstractTestCase
{
    private void badSink(int data ) throws Throwable
    {
        HashMap intHashMap = new HashMap(data);
    }
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2BSink(int data ) throws Throwable
    {
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        goodG2BSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
