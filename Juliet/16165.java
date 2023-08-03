
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_multiply_73b
{
    public void badSink(LinkedList<Long> dataLinkedList ) throws Throwable
    {
        long data = dataLinkedList.remove(2);
        if(data > 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(LinkedList<Long> dataLinkedList ) throws Throwable
    {
        long data = dataLinkedList.remove(2);
        if(data > 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(LinkedList<Long> dataLinkedList ) throws Throwable
    {
        long data = dataLinkedList.remove(2);
        if(data > 0) 
        {
            if (data < (Long.MAX_VALUE/2))
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
}
