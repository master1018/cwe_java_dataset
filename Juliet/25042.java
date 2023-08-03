
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.HashMap;
public class CWE191_Integer_Underflow__short_rand_sub_74b
{
    public void badSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        if (data > Short.MIN_VALUE)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
