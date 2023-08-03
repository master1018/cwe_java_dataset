
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_multiply_68b
{
    public void badSink() throws Throwable
    {
        byte data = CWE190_Integer_Overflow__byte_rand_multiply_68a.data;
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        byte data = CWE190_Integer_Overflow__byte_rand_multiply_68a.data;
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink() throws Throwable
    {
        byte data = CWE190_Integer_Overflow__byte_rand_multiply_68a.data;
        if(data > 0) 
        {
            if (data < (Byte.MAX_VALUE/2))
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
}
