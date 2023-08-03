
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_61b
{
    public StringBuilder badSource() throws Throwable
    {
        StringBuilder data;
        data = null;
        return data;
    }
    public StringBuilder goodG2BSource() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder();
        return data;
    }
    public StringBuilder goodB2GSource() throws Throwable
    {
        StringBuilder data;
        data = null;
        return data;
    }
}
