
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__semicolon_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (true)
        {
            ; 
            IO.writeLine("Hello from bad()");
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
            IO.writeLine("Hello from good()");
        }
    }
    private void good2() throws Throwable
    {
        if (true)
        {
            IO.writeLine("Hello from good()");
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
