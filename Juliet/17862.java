
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_multiply_68b
{
    public void badSink() throws Throwable
    {
        long data = CWE190_Integer_Overflow__long_rand_multiply_68a.data;
        if(data > 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        long data = CWE190_Integer_Overflow__long_rand_multiply_68a.data;
        if(data > 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink() throws Throwable
    {
        long data = CWE190_Integer_Overflow__long_rand_multiply_68a.data;
        if(data > 0) 
        {
            if (data < (Long.MAX_VALUE/2))
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
}
