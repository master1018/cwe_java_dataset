
package testcases.CWE23_Relative_Path_Traversal;
import testcasesupport.*;
import java.io.*;
import javax.servlet.http.*;
public class CWE23_Relative_Path_Traversal__Property_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE23_Relative_Path_Traversal__Property_22a.badPublicStatic)
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
        if (CWE23_Relative_Path_Traversal__Property_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        return data;
    }
    public String goodG2B2Source() throws Throwable
    {
        String data;
        if (CWE23_Relative_Path_Traversal__Property_22a.goodG2B2PublicStatic)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        return data;
    }
}
