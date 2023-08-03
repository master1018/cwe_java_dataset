
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_divide_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        CWE369_Divide_by_Zero__int_random_divide_81_base baseObject = new CWE369_Divide_by_Zero__int_random_divide_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        CWE369_Divide_by_Zero__int_random_divide_81_base baseObject = new CWE369_Divide_by_Zero__int_random_divide_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        CWE369_Divide_by_Zero__int_random_divide_81_base baseObject = new CWE369_Divide_by_Zero__int_random_divide_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
