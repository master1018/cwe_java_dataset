
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_square_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
        short[] dataArray = new short[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__short_max_square_66b()).badSink(dataArray  );
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
        short[] dataArray = new short[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__short_max_square_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        short data;
        data = Short.MAX_VALUE;
        short[] dataArray = new short[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__short_max_square_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
