
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_byte_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Short.MAX_VALUE + 5;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE197_Numeric_Truncation_Error__int_large_to_byte_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE197_Numeric_Truncation_Error__int_large_to_byte_66b()).goodG2BSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
