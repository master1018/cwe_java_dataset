
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import java.util.HashMap;
public class CWE191_Integer_Underflow__byte_rand_multiply_74b
{
    public void badSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        if(data < 0) 
        {
            if (data > (Byte.MIN_VALUE/2))
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
