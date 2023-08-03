
package testcases.CWE90_LDAP_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE90_LDAP_Injection__Environment_61b
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
