
package testcases.CWE674_Uncontrolled_Recursion;
import java.security.SecureRandom;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE674_Uncontrolled_Recursion__missing_base_01 extends AbstractTestCase 
{
    private static long helperBad(long level)
    {
        long longSum = level + helperBad(level-1);
        return longSum;
    }
    public void bad()
    {
        long longSecureRandom = (new SecureRandom()).nextInt(100);
        IO.writeLine(helperBad(longSecureRandom));
    }
    private static long helperGood1(long level)
    {
        if (level < 0)
        {
            return 0;
        }
        long longSum = level + helperGood1(level-1);
        return longSum;
    }
    private void good1()
    {
        long longSecureRandom = (new SecureRandom()).nextInt(100);
        IO.writeLine(helperGood1(longSecureRandom));
    }
    public void good()  
    {
        good1();
    }    
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
