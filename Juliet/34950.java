
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_square_45 extends AbstractTestCase
{
    private short dataBad;
    private short dataGoodG2B;
    private short dataGoodB2G;
    private void badSink() throws Throwable
    {
        short data = dataBad;
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void bad() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink() throws Throwable
    {
        short data = dataGoodG2B;
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    private void goodG2B() throws Throwable
    {
        short data;
        data = 2;
        dataGoodG2B = data;
        goodG2BSink();
    }
    private void goodB2GSink() throws Throwable
    {
        short data = dataGoodB2G;
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Short.MAX_VALUE)))
        {
            short result = (short)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        dataGoodB2G = data;
        goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
