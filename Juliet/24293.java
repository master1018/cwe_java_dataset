
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__Integer_61b
{
    public Integer badSource() throws Throwable
    {
        Integer data;
        data = null;
        return data;
    }
    public Integer goodG2BSource() throws Throwable
    {
        Integer data;
        data = Integer.valueOf(5);
        return data;
    }
    public Integer goodB2GSource() throws Throwable
    {
        Integer data;
        data = null;
        return data;
    }
}
