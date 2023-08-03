
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_22b
{
    public void badSink(int [] data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__int_array_22a.badPublicStatic)
        {
            IO.writeLine("" + data.length);
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(int [] data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__int_array_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                IO.writeLine("" + data.length);
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
    }
    public void goodB2G2Sink(int [] data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__int_array_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                IO.writeLine("" + data.length);
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(int [] data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__int_array_22a.goodG2BPublicStatic)
        {
            IO.writeLine("" + data.length);
        }
        else
        {
            data = null;
        }
    }
}
