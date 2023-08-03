
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_add_67a extends AbstractTestCase
{
    static class Container
    {
        public byte containerOne;
    }
    public void bad() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__byte_rand_add_67b()).badSink(dataContainer  );
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
        (new CWE190_Integer_Overflow__byte_rand_add_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__byte_rand_add_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
