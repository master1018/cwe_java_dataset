
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_multiply_67b
{
    public void badSink(CWE190_Integer_Overflow__byte_max_multiply_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(CWE190_Integer_Overflow__byte_max_multiply_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(CWE190_Integer_Overflow__byte_max_multiply_67a.Container dataContainer ) throws Throwable
    {
        byte data = dataContainer.containerOne;
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
