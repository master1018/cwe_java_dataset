
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE197_Numeric_Truncation_Error__short_listen_tcp_73b
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
