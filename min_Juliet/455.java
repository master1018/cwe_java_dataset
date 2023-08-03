
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE570_Expression_Always_False__two_equals_three_01 extends AbstractTestCase 
{
    public void bad()
    {
        if (2 == 3)
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
