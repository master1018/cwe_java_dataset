
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_61b()).badSource();
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_61b()).goodG2BSource();
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private void goodB2G() throws Throwable
    {
        String data = (new CWE690_NULL_Deref_From_Return__System_getProperty_trim_61b()).goodB2GSource();
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
