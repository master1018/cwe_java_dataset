
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
import java.util.Vector;
public class CWE197_Numeric_Truncation_Error__int_console_readLine_to_short_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        {
            IO.writeLine((short)data);
        }
    }
}
