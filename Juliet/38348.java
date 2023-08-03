
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__length_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (true)
        {
            assert "".length() > 0;
        }
    }
    private void good1() throws Throwable
    {
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            assert "cwe617".length() > 0;
        }
    }
    private void good2() throws Throwable
    {
        if (true)
        {
            assert "cwe617".length() > 0;
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
