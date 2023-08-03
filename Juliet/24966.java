
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_42 extends AbstractTestCase
{
    private String badSource() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        return data;
    }
    public void bad() throws Throwable
    {
        String data = badSource();
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    private String goodG2BSource() throws Throwable
    {
        String data;
        data = "CWE690";
        return data;
    }
    private void goodG2B() throws Throwable
    {
        String data = goodG2BSource();
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    private String goodB2GSource() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        return data;
    }
    private void goodB2G() throws Throwable
    {
        String data = goodB2GSource();
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
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
