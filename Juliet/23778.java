
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_square_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__int_max_square_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__int_max_square_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE190_Integer_Overflow__int_max_square_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
