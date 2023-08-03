
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.HashMap;
public class CWE197_Numeric_Truncation_Error__short_large_74b
{
    public void badSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        {
            IO.writeLine((byte)data);
        }
    }
}
