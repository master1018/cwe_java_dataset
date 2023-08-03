
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_61b
{
    public int badSource() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        return count;
    }
    public int goodG2BSource() throws Throwable
    {
        int count;
        count = 2;
        return count;
    }
    public int goodB2GSource() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        return count;
    }
}
