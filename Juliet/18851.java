
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.LinkedList;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__short_random_73a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new SecureRandom()).nextInt(Short.MAX_VALUE + 1));
        LinkedList<Short> dataLinkedList = new LinkedList<Short>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE197_Numeric_Truncation_Error__short_random_73b()).badSink(dataLinkedList  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        LinkedList<Short> dataLinkedList = new LinkedList<Short>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE197_Numeric_Truncation_Error__short_random_73b()).goodG2BSink(dataLinkedList  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
