
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import java.util.Vector;
public class CWE191_Integer_Underflow__short_rand_multiply_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        Vector<Short> dataVector = new Vector<Short>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE191_Integer_Underflow__short_rand_multiply_72b()).badSink(dataVector  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        Vector<Short> dataVector = new Vector<Short>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE191_Integer_Underflow__short_rand_multiply_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        Vector<Short> dataVector = new Vector<Short>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE191_Integer_Underflow__short_rand_multiply_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
