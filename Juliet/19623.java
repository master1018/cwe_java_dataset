
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        for (int j = 0; j < 1; j++)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        for (int j = 0; j < 1; j++)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        for (int k = 0; k < 1; k++)
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
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
