
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.HashMap;
public class CWE476_NULL_Pointer_Dereference__Integer_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        Integer data = dataHashMap.get(2);
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        Integer data = dataHashMap.get(2);
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        Integer data = dataHashMap.get(2);
        if (data != null)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
