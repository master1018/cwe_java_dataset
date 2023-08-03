
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_no_check_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = -1;
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_no_check_73b()).badSink(dataLinkedList  );
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
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_no_check_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = -1;
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_no_check_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
