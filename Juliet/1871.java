
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Property_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__Property_22a.badPublicStatic)
        {
            data = System.getProperty("user.home");
        }
        else
        {
            data = null;
        }
        return data;
    }
    public String goodG2B1Source() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__Property_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "Testing.test";
        }
        return data;
    }
    public String goodG2B2Source() throws Throwable
    {
        String data;
        if (CWE470_Unsafe_Reflection__Property_22a.goodG2B2PublicStatic)
        {
            data = "Testing.test";
        }
        else
        {
            data = null;
        }
        return data;
    }
}
