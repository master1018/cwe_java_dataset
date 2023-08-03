
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        Vector<Byte> dataVector = new Vector<Byte>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE190_Integer_Overflow__byte_max_add_72b()).badSink(dataVector  );
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
        Vector<Byte> dataVector = new Vector<Byte>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE190_Integer_Overflow__byte_max_add_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MAX_VALUE;
        Vector<Byte> dataVector = new Vector<Byte>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE190_Integer_Overflow__byte_max_add_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
