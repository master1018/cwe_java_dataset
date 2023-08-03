
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_61b
{
    public int [] badSource() throws Throwable
    {
        int [] data;
        data = null;
        return data;
    }
    public int [] goodG2BSource() throws Throwable
    {
        int [] data;
        data = new int[5];
        return data;
    }
    public int [] goodB2GSource() throws Throwable
    {
        int [] data;
        data = null;
        return data;
    }
}
