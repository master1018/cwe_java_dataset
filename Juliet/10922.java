
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_67a extends AbstractTestCase
{
    static class Container
    {
        public long containerOne;
    }
    public void bad() throws Throwable
    {
        long data;
        data = (new java.security.SecureRandom()).nextLong();
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__long_rand_sub_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__long_rand_sub_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = (new java.security.SecureRandom()).nextLong();
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__long_rand_sub_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
