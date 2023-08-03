
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_square_67a extends AbstractTestCase
{
    static class Container
    {
        public short containerOne;
    }
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__short_rand_square_67b()).badSink(dataContainer  );
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
        (new CWE190_Integer_Overflow__short_rand_square_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__short_rand_square_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
