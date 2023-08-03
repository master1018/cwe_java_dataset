
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_sub_61b
{
    public short badSource() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        return data;
    }
    public short goodG2BSource() throws Throwable
    {
        short data;
        data = 2;
        return data;
    }
    public short goodB2GSource() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        return data;
    }
}
