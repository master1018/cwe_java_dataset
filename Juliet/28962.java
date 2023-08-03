
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_divide_67a extends AbstractTestCase
{
    static class Container
    {
        public float containerOne;
    }
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE369_Divide_by_Zero__float_zero_divide_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE369_Divide_by_Zero__float_zero_divide_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        data = 0.0f; 
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE369_Divide_by_Zero__float_zero_divide_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
