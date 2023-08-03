
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE191_Integer_Underflow__long_console_readLine_multiply_73b
{
    public void badSink(LinkedList<Long> dataLinkedList ) throws Throwable
    {
        long data = dataLinkedList.remove(2);
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(LinkedList<Long> dataLinkedList ) throws Throwable
    {
        long data = dataLinkedList.remove(2);
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(LinkedList<Long> dataLinkedList ) throws Throwable
    {
        long data = dataLinkedList.remove(2);
        if(data < 0) 
        {
            if (data > (Long.MIN_VALUE/2))
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
