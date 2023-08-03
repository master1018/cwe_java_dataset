
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_22b
{
    public void badSink(Integer data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__Integer_22a.badPublicStatic)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(Integer data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__Integer_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                IO.writeLine("" + data.toString());
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
    }
    public void goodB2G2Sink(Integer data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__Integer_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                IO.writeLine("" + data.toString());
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
    public void goodG2BSink(Integer data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__Integer_22a.goodG2BPublicStatic)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            data = null;
        }
    }
}
