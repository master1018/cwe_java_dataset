
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_modulo_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = 0; 
        (new CWE369_Divide_by_Zero__int_zero_modulo_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE369_Divide_by_Zero__int_zero_modulo_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = 0; 
        (new CWE369_Divide_by_Zero__int_zero_modulo_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
