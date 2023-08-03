
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE191_Integer_Underflow__short_console_readLine_sub_73b
{
    public void badSink(LinkedList<Short> dataLinkedList ) throws Throwable
    {
        short data = dataLinkedList.remove(2);
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(LinkedList<Short> dataLinkedList ) throws Throwable
    {
        short data = dataLinkedList.remove(2);
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(LinkedList<Short> dataLinkedList ) throws Throwable
    {
        short data = dataLinkedList.remove(2);
        if (data > Short.MIN_VALUE)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
