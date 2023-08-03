
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_database_square_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
}
