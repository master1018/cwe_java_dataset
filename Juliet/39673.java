
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_add_67a extends AbstractTestCase
{
    static class Container
    {
        public long containerOne;
    }
    public void bad() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__long_max_add_67b()).badSink(dataContainer  );
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
        (new CWE190_Integer_Overflow__long_max_add_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MAX_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE190_Integer_Overflow__long_max_add_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
