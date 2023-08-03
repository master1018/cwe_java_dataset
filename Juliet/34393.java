
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_22b
{
    public void badSink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_String_22a.badPublicStatic)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_String_22a.goodB2G1PublicStatic)
        {
            data = null;
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
    public void goodB2G2Sink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_String_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(String data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_String_22a.goodG2BPublicStatic)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            data = null;
        }
    }
}
