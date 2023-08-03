
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_22b
{
    public void badSink(StringBuilder data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_StringBuilder_22a.badPublicStatic)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(StringBuilder data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_StringBuilder_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    public void goodB2G2Sink(StringBuilder data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_StringBuilder_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(StringBuilder data ) throws Throwable
    {
        if (CWE690_NULL_Deref_From_Return__Class_StringBuilder_22a.goodG2BPublicStatic)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            data = null;
        }
    }
}
