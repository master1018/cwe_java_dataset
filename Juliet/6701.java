
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_52c
{
    public void badSink(String data ) throws Throwable
    {
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink(String data ) throws Throwable
    {
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
