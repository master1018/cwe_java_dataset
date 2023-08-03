
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_multiply_67a extends AbstractTestCase
{
    static class Container
    {
        public short containerOne;
    }
    public void bad() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__short_min_multiply_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__short_min_multiply_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = Short.MIN_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__short_min_multiply_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
