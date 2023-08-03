
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        while (true)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
            break;
        }
        while (true)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        while (true)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
            break;
        }
        while (true)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        while (true)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
            break;
        }
        while (true)
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
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
