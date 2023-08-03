
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE197_Numeric_Truncation_Error__int_PropertiesFile_to_short_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList ) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        {
            IO.writeLine((short)data);
        }
    }
}
