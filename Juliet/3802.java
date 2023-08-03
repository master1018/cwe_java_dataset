
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_square_61b
{
    public byte badSource() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        return data;
    }
    public byte goodG2BSource() throws Throwable
    {
        byte data;
        data = 2;
        return data;
    }
    public byte goodB2GSource() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        return data;
    }
}
