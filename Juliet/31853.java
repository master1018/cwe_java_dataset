
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_67b
{
    public void badSink(CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink(CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink(CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
