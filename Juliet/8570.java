
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Property_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE606_Unchecked_Loop_Condition__Property_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "5";
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE606_Unchecked_Loop_Condition__Property_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        LinkedList<String> dataLinkedList = new LinkedList<String>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE606_Unchecked_Loop_Condition__Property_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
