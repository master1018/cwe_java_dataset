
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Property_61b
{
    public String badSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
    public String goodG2BSource() throws Throwable
    {
        String data;
        data = "5";
        return data;
    }
    public String goodB2GSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
}
