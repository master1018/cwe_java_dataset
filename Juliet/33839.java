
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_22b
{
    public void badSink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__System_getProperty_equals_22a.badPublicStatic)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__System_getProperty_equals_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
    }
    public void goodB2G2Sink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__System_getProperty_equals_22a.goodB2G2PublicStatic)
        {
            if("CWE690".equals(data))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__System_getProperty_equals_22a.goodG2BPublicStatic)
        {
            if(data.equals("CWE690"))
            {
                IO.writeLine("data is CWE690");
            }
        }
        else
        {
            data = null;
        }
    }
}
