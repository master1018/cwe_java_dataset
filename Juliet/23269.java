
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_61b
{
    public long badSource() throws Throwable
    {
        long data;
        data = (new java.security.SecureRandom()).nextLong();
        return data;
    }
    public long goodG2BSource() throws Throwable
    {
        long data;
        data = 2;
        return data;
    }
    public long goodB2GSource() throws Throwable
    {
        long data;
        data = (new java.security.SecureRandom()).nextLong();
        return data;
    }
}
