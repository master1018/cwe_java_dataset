
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE789_Uncontrolled_Mem_Alloc__random_ArrayList_73b()).goodG2BSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}