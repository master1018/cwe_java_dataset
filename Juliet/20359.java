
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        byte[] dataArray = new byte[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__byte_max_add_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        byte[] dataArray = new byte[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__byte_max_add_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        byte[] dataArray = new byte[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__byte_max_add_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
