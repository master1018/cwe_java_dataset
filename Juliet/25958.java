
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.util.LinkedList;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_divide_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        LinkedList<Float> dataLinkedList = new LinkedList<Float>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE369_Divide_by_Zero__float_random_divide_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        LinkedList<Float> dataLinkedList = new LinkedList<Float>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE369_Divide_by_Zero__float_random_divide_73b()).goodG2BSink(dataLinkedList  );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        LinkedList<Float> dataLinkedList = new LinkedList<Float>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE369_Divide_by_Zero__float_random_divide_73b()).goodB2GSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
