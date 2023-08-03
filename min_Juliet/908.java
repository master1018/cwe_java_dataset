
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
public class CWE571_Expression_Always_True__private_five_01 extends AbstractTestCase 
{
    private int privateFive = 5;
    public void bad()
    {
        if (privateFive == 5)
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
        if ((new SecureRandom()).nextInt() == privateFive)
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
