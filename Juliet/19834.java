
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        while (true)
        {
            data = System.getProperty("CWE690");
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
            data = "CWE690";
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
            data = System.getProperty("CWE690");
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
