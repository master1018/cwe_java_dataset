
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.Vector;
public class CWE197_Numeric_Truncation_Error__short_large_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        Vector<Short> dataVector = new Vector<Short>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE197_Numeric_Truncation_Error__short_large_72b()).badSink(dataVector  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        Vector<Short> dataVector = new Vector<Short>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE197_Numeric_Truncation_Error__short_large_72b()).goodG2BSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
