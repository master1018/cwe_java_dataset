
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
            dataCopy = data;
        }
        {
            String data = dataCopy;
            String stringTrimmed = data.trim();
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
        String dataCopy;
        {
            String data;
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
            dataCopy = data;
        }
        {
            String data = dataCopy;
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
            dataCopy = data;
        }
        {
            String data = dataCopy;
            if (data != null)
            {
                String stringTrimmed = data.trim();
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
