
package testcases.CWE23_Relative_Path_Traversal;
import testcasesupport.*;
import java.io.*;
import javax.servlet.http.*;
public class CWE23_Relative_Path_Traversal__Environment_61b
{
    public String badSource() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        return data;
    }
    public String goodG2BSource() throws Throwable
    {
        String data;
        data = "foo";
        return data;
    }
}
