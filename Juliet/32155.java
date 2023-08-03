
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_square_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        long[] dataArray = new long[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__long_max_square_66b()).badSink(dataArray  );
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
        long[] dataArray = new long[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__long_max_square_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        long[] dataArray = new long[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__long_max_square_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
