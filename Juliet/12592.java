
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_67a extends AbstractTestCase
{
    static class Container
    {
        public short containerOne;
    }
    public void bad() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE197_Numeric_Truncation_Error__short_large_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE197_Numeric_Truncation_Error__short_large_67b()).goodG2BSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
