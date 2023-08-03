
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_trim_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if (IO.staticFive==5)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.staticFive==5)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (IO.staticFive!=5)
        {
            data = null;
        }
        else
        {
            data = "CWE690";
        }
        if (IO.staticFive==5)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (IO.staticFive==5)
        {
            data = "CWE690";
        }
        else
        {
            data = null;
        }
        if (IO.staticFive==5)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (IO.staticFive==5)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.staticFive!=5)
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
        if (IO.staticFive==5)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.staticFive==5)
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
