
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_42 extends AbstractTestCase
{
    private StringBuilder badSource() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        return data;
    }
    public void bad() throws Throwable
    {
        StringBuilder data = badSource();
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    private StringBuilder goodG2BSource() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        return data;
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data = goodG2BSource();
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    private StringBuilder goodB2GSource() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        return data;
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data = goodB2GSource();
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
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
