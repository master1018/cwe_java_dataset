
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_square_61b
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
