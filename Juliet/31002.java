
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_square_67a extends AbstractTestCase
{
    static class Container
    {
        public int containerOne;
    }
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__int_max_square_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__int_max_square_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__int_max_square_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
