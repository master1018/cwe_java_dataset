
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_31 extends AbstractTestCase
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
            ArrayList intArrayList = new ArrayList(data);
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
            ArrayList intArrayList = new ArrayList(data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
