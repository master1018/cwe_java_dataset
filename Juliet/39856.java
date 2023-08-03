
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.HashMap;
public class CWE476_NULL_Pointer_Dereference__int_array_74b
{
    public void badSink(HashMap<Integer,int []> dataHashMap ) throws Throwable
    {
        int [] data = dataHashMap.get(2);
        IO.writeLine("" + data.length);
    }
    public void goodG2BSink(HashMap<Integer,int []> dataHashMap ) throws Throwable
    {
        int [] data = dataHashMap.get(2);
        IO.writeLine("" + data.length);
    }
    public void goodB2GSink(HashMap<Integer,int []> dataHashMap ) throws Throwable
    {
        int [] data = dataHashMap.get(2);
        if (data != null)
        {
            IO.writeLine("" + data.length);
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
