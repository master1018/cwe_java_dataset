
package testcases.CWE89_SQL_Injection.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__Property_executeUpdate_61b
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
        data = "foo";
        return data;
    }
    public String goodB2GSource() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        return data;
    }
}
