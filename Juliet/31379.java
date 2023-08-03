
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_console_readLine_sub_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        if (data > Integer.MIN_VALUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
}
