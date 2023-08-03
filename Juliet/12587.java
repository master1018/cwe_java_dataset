
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE570_Expression_Always_False__private_return_01 extends AbstractTestCase 
{
    private boolean privateReturnsFalse() 
    {
        return false;
    }
    public void bad()
    {
        if (privateReturnsFalse())
        {
            IO.writeLine("never prints");
        }
    }
    public void good()
    {
        good1();
    }
    private void good1()
    {
        if (IO.staticReturnsTrueOrFalse() == privateReturnsFalse())
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
