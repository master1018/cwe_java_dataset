
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_function_01 extends AbstractTestCase 
{
    private void helperBad() 
    {
    }
    public void bad()
    {
        helperBad();
    }
    private void helperGood1() 
    {
        IO.writeLine("helperGood1()");
    }
    private void good1()
    {
        helperGood1();
    }
    public void good()
    {
        good1();
    }
    public static void main(String[] args) 
        throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
