
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashSet_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashSet_61b()).badSource();
        HashSet intHashSet = new HashSet(data);
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__URLConnection_HashSet_61b()).goodG2BSource();
        HashSet intHashSet = new HashSet(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
