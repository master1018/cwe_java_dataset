
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        while (true)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            break;
        }
        while (true)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        while (true)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
            break;
        }
        while (true)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        while (true)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            break;
        }
        while (true)
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
