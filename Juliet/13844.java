
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__false_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            assert false; 
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            assert true; 
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
