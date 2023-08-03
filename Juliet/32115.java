
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FALSE)
        {
            data = null;
        }
        else
        {
            data = "CWE690";
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = "CWE690";
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
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
    private void goodB2G2() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_TRUE)
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
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
