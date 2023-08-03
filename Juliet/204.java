
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_41 extends AbstractTestCase
{
    private void badSink(String data ) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void bad() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(String data ) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        goodG2BSink(data  );
    }
    private void goodB2GSink(String data ) throws Throwable
    {
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
