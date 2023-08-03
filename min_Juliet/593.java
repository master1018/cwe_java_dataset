
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
public class CWE571_Expression_Always_True__n_less_int_max_01 extends AbstractTestCase 
{
    public void bad()
    {
        int intSecureRandom = (new SecureRandom()).nextInt();
        if (intSecureRandom < Integer.MAX_VALUE)
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
        int intSecureRandom = (new SecureRandom()).nextInt();
        if (intSecureRandom < 5000)
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
