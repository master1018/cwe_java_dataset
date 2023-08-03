
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.util.Vector;
public class CWE369_Divide_by_Zero__float_zero_modulo_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        Vector<Float> dataVector = new Vector<Float>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE369_Divide_by_Zero__float_zero_modulo_72b()).badSink(dataVector  );
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
        Vector<Float> dataVector = new Vector<Float>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE369_Divide_by_Zero__float_zero_modulo_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        data = 0.0f; 
        Vector<Float> dataVector = new Vector<Float>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE369_Divide_by_Zero__float_zero_modulo_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
