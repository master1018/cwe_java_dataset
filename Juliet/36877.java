
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__listen_tcp_ArrayList_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__listen_tcp_ArrayList_61b()).badSource();
        ArrayList intArrayList = new ArrayList(data);
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__listen_tcp_ArrayList_61b()).goodG2BSource();
        ArrayList intArrayList = new ArrayList(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
