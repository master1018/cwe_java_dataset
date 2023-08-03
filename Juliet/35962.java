
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = "CWE690";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "CWE690";
        }
        else
        {
            data = "CWE690";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = System.getProperty("CWE690");
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
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
