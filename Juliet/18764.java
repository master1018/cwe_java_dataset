
package testcases.CWE197_Numeric_Truncation_Error.s03;
import testcasesupport.*;
import java.util.Vector;
public class CWE197_Numeric_Truncation_Error__short_URLConnection_72b
{
    public void badSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(Vector<Short> dataVector ) throws Throwable
    {
        short data = dataVector.remove(2);
        {
            IO.writeLine((byte)data);
        }
    }
}
