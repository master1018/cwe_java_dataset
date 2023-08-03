
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_multiply_53d
{
    public void badSink(byte data ) throws Throwable
    {
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(byte data ) throws Throwable
    {
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
