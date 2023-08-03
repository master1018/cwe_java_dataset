
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_add_74b
{
    public void badSink(HashMap<Integer,Long> dataHashMap ) throws Throwable
    {
        long data = dataHashMap.get(2);
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HashMap<Integer,Long> dataHashMap ) throws Throwable
    {
        long data = dataHashMap.get(2);
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HashMap<Integer,Long> dataHashMap ) throws Throwable
    {
        long data = dataHashMap.get(2);
        if (data < Long.MAX_VALUE)
        {
            long result = (long)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
