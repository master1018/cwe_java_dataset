
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if (IO.staticTrue)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.staticTrue)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (IO.staticFalse)
        {
            data = null;
        }
        else
        {
            data = "CWE690";
        }
        if (IO.staticTrue)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (IO.staticTrue)
        {
            data = "CWE690";
        }
        else
        {
            data = null;
        }
        if (IO.staticTrue)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (IO.staticTrue)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (IO.staticTrue)
        {
            data = System.getProperty("CWE690");
        }
        else
        {
            data = null;
        }
        if (IO.staticTrue)
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
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
