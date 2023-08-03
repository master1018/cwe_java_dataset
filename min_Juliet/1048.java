
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
import java.util.Random;
public class CWE571_Expression_Always_True__class_getClass_not_equal_01 extends AbstractTestCase
{    
    public void bad()
    {
        Random random = new Random();
        SecureRandom secureRandom = new SecureRandom();
        if (!random.getClass().equals(secureRandom.getClass()))
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
        Object objectArray[] = new Object [] {new Random(), new SecureRandom(), new SecureRandom()};
        int intSecureRandom = (new SecureRandom()).nextInt(3);
        if (objectArray[1].getClass().equals(objectArray[intSecureRandom].getClass()))
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
