
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_multiply_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        byte[] dataArray = new byte[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__byte_rand_multiply_66b()).badSink(dataArray  );
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
        byte[] dataArray = new byte[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__byte_rand_multiply_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = (byte)((new java.security.SecureRandom()).nextInt(1+Byte.MAX_VALUE-Byte.MIN_VALUE) + Byte.MIN_VALUE);
        byte[] dataArray = new byte[5];
        dataArray[2] = data;
        (new CWE191_Integer_Underflow__byte_rand_multiply_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
