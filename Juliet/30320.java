
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_multiply_74b
{
    public void badSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        if(data > 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
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
