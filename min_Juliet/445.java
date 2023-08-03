
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__semicolon_06 extends AbstractTestCase
{
    private static final int PRIVATE_STATIC_FINAL_FIVE = 5;
    public void bad() throws Throwable
    {
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
            ; 
            IO.writeLine("Hello from bad()");
        }
    }
    private void good1() throws Throwable
    {
        if (PRIVATE_STATIC_FINAL_FIVE != 5)
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
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
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
