
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_sub_61b
{
    public short badSource() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
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
        data = Short.MIN_VALUE;
        return data;
    }
}
