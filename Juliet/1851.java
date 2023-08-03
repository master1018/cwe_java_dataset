
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__semicolon_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        ; 
        IO.writeLine("Hello from bad()");
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        IO.writeLine("Hello from good()");
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
