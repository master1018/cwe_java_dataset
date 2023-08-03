
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE570_Expression_Always_False__private_01 extends AbstractTestCase 
{
    private boolean privateFalse = false;
    public void bad()
    {
        if (privateFalse)
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
        if (IO.staticReturnsTrueOrFalse() == privateFalse)
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
