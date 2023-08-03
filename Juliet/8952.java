
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__length_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            assert "".length() > 0;
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            assert "cwe617".length() > 0;
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
