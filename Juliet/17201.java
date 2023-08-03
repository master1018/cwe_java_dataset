
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.Vector;
public class CWE191_Integer_Underflow__long_min_multiply_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        Vector<Long> dataVector = new Vector<Long>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE191_Integer_Underflow__long_min_multiply_72b()).badSink(dataVector  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        Vector<Long> dataVector = new Vector<Long>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE191_Integer_Underflow__long_min_multiply_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        Vector<Long> dataVector = new Vector<Long>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE191_Integer_Underflow__long_min_multiply_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
