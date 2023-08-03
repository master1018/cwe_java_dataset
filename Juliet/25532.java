
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_multiply_67a extends AbstractTestCase
{
    static class Container
    {
        public byte containerOne;
    }
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__byte_min_multiply_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__byte_min_multiply_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__byte_min_multiply_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
