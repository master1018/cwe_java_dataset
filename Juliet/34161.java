
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
public class CWE571_Expression_Always_True__n_equal_m_minus_one_01 extends AbstractTestCase 
{
    public void bad()
    {
        int intThirty = 30;
        int intThirtyOne = 31;
        if (intThirty == (intThirtyOne - 1))
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
        int intSecureRandom1 = (new SecureRandom()).nextInt();
        int intSecureRandom2 = (new SecureRandom()).nextInt();
        if (intSecureRandom1 != intSecureRandom2)
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
