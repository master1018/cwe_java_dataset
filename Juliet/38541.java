
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_61b
{
    public String badSource() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        return data;
    }
    public String goodG2BSource() throws Throwable
    {
        String data;
        data = "CWE690";
        return data;
    }
    public String goodB2GSource() throws Throwable
    {
        String data;
        data = System.getProperty("CWE690");
        return data;
    }
}
