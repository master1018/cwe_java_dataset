
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_42 extends AbstractTestCase
{
    private String badSource() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        return data;
    }
    public void bad() throws Throwable
    {
        String data = badSource();
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private String goodG2BSource() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        return data;
    }
    private void goodG2B() throws Throwable
    {
        String data = goodG2BSource();
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private String goodB2GSource() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        return data;
    }
    private void goodB2G() throws Throwable
    {
        String data = goodB2GSource();
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
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
