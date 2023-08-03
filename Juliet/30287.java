
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE134_Uncontrolled_Format_String__Environment_format_73b
{
    public void badSink(LinkedList<String> dataLinkedList ) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodG2BSink(LinkedList<String> dataLinkedList ) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodB2GSink(LinkedList<String> dataLinkedList ) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
}
