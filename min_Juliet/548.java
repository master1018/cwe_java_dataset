
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
public class CWE570_Expression_Always_False__private_static_final_five_01 extends AbstractTestCase 
{
    private static final int PRIVATE_STATIC_FINAL_FIVE = 5;
    public void bad()
    {
        if (PRIVATE_STATIC_FINAL_FIVE != 5)
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
        if ((new SecureRandom()).nextInt() != PRIVATE_STATIC_FINAL_FIVE)
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
