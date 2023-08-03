
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__false_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        assert false; 
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        assert true; 
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
