
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__false_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
        {
            assert false; 
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
        {
            assert true; 
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
