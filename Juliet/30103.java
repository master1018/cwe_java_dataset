
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data = (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_61b()).badSource();
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data = (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_61b()).goodG2BSource();
        String stringTrimmed = data.toString().trim();
        IO.writeLine(stringTrimmed);
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data = (new CWE690_NULL_Deref_From_Return__Class_StringBuilder_61b()).goodB2GSource();
        if (data != null)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
