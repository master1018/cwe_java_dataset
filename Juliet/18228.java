
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_multiply_61b
{
    public long badSource() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
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
        data = Long.MAX_VALUE;
        return data;
    }
}
