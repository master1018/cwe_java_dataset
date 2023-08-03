
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_61b
{
    public short badSource() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
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
        data = Short.MAX_VALUE;
        return data;
    }
}
