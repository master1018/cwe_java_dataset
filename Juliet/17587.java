
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_min_sub_67a extends AbstractTestCase
{
    static class Container
    {
        public int containerOne;
    }
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__int_min_sub_67b()).badSink(dataContainer  );
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
        (new CWE191_Integer_Underflow__int_min_sub_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE191_Integer_Underflow__int_min_sub_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
