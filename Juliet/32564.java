
package testcases.CWE90_LDAP_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE90_LDAP_Injection__Property_61b
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
}
