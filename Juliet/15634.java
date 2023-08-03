
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_68b
{
    public void badSink() throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__System_getProperty_equals_68a.data;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink() throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__System_getProperty_equals_68a.data;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink() throws Throwable
    {
        String data = CWE690_NULL_Deref_From_Return__System_getProperty_equals_68a.data;
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
