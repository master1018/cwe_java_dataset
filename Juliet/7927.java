
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder dataCopy;
        {
            StringBuilder data;
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            dataCopy = data;
        }
        {
            StringBuilder data = dataCopy;
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder dataCopy;
        {
            StringBuilder data;
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
            dataCopy = data;
        }
        {
            StringBuilder data = dataCopy;
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder dataCopy;
        {
            StringBuilder data;
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            dataCopy = data;
        }
        {
            StringBuilder data = dataCopy;
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
