
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_divide_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        (new CWE369_Divide_by_Zero__float_zero_divide_51b()).badSink(data  );
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
        (new CWE369_Divide_by_Zero__float_zero_divide_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        data = 0.0f; 
        (new CWE369_Divide_by_Zero__float_zero_divide_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
