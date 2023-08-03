
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
import java.util.Random;
public class CWE570_Expression_Always_False__class_getClass_equal_01 extends AbstractTestCase
{    
    public void bad()
    {
        Random random = new Random();
        SecureRandom secureRandom = new SecureRandom();
        if (random.getClass().equals(secureRandom.getClass()))
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
