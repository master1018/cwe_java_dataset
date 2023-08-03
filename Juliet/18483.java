
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE571_Expression_Always_True__true_01 extends AbstractTestCase 
{
    public void bad()
    {
        if (true)
        {
            IO.writeLine("always prints");
        }
    }
    public void good()
    {
        good1();
    }
    private void good1()
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            IO.writeLine("sometimes prints");
        }
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
