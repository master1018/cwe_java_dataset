
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_sub_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        long[] dataArray = new long[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__long_min_sub_66b()).badSink(dataArray  );
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
        long[] dataArray = new long[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__long_min_sub_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        long[] dataArray = new long[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__long_min_sub_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
