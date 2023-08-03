
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_square_61b
{
    public int badSource() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        return data;
    }
    public int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    public int goodB2GSource() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        return data;
    }
}
