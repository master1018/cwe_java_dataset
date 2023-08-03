
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__length_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        assert "".length() > 0;
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        assert "cwe617".length() > 0;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
