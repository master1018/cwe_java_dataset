
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__random_for_loop_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        LinkedList<Integer> countLinkedList = new LinkedList<Integer>();
        countLinkedList.add(0, count);
        countLinkedList.add(1, count);
        countLinkedList.add(2, count);
        (new CWE400_Resource_Exhaustion__random_for_loop_73b()).badSink(countLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        LinkedList<Integer> countLinkedList = new LinkedList<Integer>();
        countLinkedList.add(0, count);
        countLinkedList.add(1, count);
        countLinkedList.add(2, count);
        (new CWE400_Resource_Exhaustion__random_for_loop_73b()).goodG2BSink(countLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        LinkedList<Integer> countLinkedList = new LinkedList<Integer>();
        countLinkedList.add(0, count);
        countLinkedList.add(1, count);
        countLinkedList.add(2, count);
        (new CWE400_Resource_Exhaustion__random_for_loop_73b()).goodB2GSink(countLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
