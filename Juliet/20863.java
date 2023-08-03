
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_66b
{
    public void badSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink(String dataArray[] ) throws Throwable
    {
        String data = dataArray[2];
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
