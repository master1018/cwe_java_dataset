
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_61b
{
    public String badSource() throws Throwable
    {
        String data;
        data = null;
        return data;
    }
    public String goodG2BSource() throws Throwable
    {
        String data;
        data = "This is not null";
        return data;
    }
    public String goodB2GSource() throws Throwable
    {
        String data;
        data = null;
        return data;
    }
}
