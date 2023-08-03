
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.HashMap;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_74b
{
    public void badSink(HashMap<Integer,StringBuilder> dataHashMap ) throws Throwable
    {
        StringBuilder data = dataHashMap.get(2);
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(HashMap<Integer,StringBuilder> dataHashMap ) throws Throwable
    {
        StringBuilder data = dataHashMap.get(2);
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(HashMap<Integer,StringBuilder> dataHashMap ) throws Throwable
    {
        StringBuilder data = dataHashMap.get(2);
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
