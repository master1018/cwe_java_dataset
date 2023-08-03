
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE476_NULL_Pointer_Dereference__int_array_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int [] data;
        data = null;
        LinkedList<int []> dataLinkedList = new LinkedList<int []>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__int_array_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int [] data;
        data = new int[5];
        LinkedList<int []> dataLinkedList = new LinkedList<int []>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__int_array_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        int [] data;
        data = null;
        LinkedList<int []> dataLinkedList = new LinkedList<int []>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__int_array_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
