
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_add_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE190_Integer_Overflow__int_max_add_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE190_Integer_Overflow__int_max_add_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE190_Integer_Overflow__int_max_add_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
