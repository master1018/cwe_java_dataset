
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashSet_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        CWE789_Uncontrolled_Mem_Alloc__random_HashSet_81_base baseObject = new CWE789_Uncontrolled_Mem_Alloc__random_HashSet_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        CWE789_Uncontrolled_Mem_Alloc__random_HashSet_81_base baseObject = new CWE789_Uncontrolled_Mem_Alloc__random_HashSet_81_goodG2B();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
