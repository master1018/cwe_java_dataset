
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.LinkedList;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = null;
        LinkedList<StringBuilder> dataLinkedList = new LinkedList<StringBuilder>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder();
        LinkedList<StringBuilder> dataLinkedList = new LinkedList<StringBuilder>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = null;
        LinkedList<StringBuilder> dataLinkedList = new LinkedList<StringBuilder>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE476_NULL_Pointer_Dereference__StringBuilder_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
