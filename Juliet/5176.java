
package testcases.CWE197_Numeric_Truncation_Error.s03;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE197_Numeric_Truncation_Error__short_URLConnection_73b
{
    public void badSink(LinkedList<Short> dataLinkedList ) throws Throwable
    {
        short data = dataLinkedList.remove(2);
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(LinkedList<Short> dataLinkedList ) throws Throwable
    {
        short data = dataLinkedList.remove(2);
        {
            IO.writeLine((byte)data);
        }
    }
}
