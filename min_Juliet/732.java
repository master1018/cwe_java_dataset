
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE571_Expression_Always_True__private_return_01 extends AbstractTestCase 
{
    private boolean privateReturnsTrue() 
    {
        return true;
    }
    public void bad()
    {
        if (privateReturnsTrue())
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
        if (IO.staticReturnsTrueOrFalse() == privateReturnsTrue())
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