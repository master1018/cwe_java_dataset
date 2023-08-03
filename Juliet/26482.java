
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
public class CWE570_Expression_Always_False__n_less_int_min_01 extends AbstractTestCase 
{
    public void bad()
    {
        int intSecureRandom = (new SecureRandom()).nextInt();
        if (intSecureRandom < Integer.MIN_VALUE)
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
