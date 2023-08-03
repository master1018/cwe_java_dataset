
package testcases.CWE315_Plaintext_Storage_in_Cookie;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.MessageDigest;
public class CWE315_Plaintext_Storage_in_Cookie__Servlet_67b
{
    public void badSink(CWE315_Plaintext_Storage_in_Cookie__Servlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        response.addCookie(new Cookie("auth", data));
    }
    public void goodG2BSink(CWE315_Plaintext_Storage_in_Cookie__Servlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        response.addCookie(new Cookie("auth", data));
    }
    public void goodB2GSink(CWE315_Plaintext_Storage_in_Cookie__Servlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        {
            String salt = "ThisIsMySalt";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            byte[] hashedCredsAsBytes = messageDigest.digest((salt+data).getBytes("UTF-8"));
            data = IO.toHex(hashedCredsAsBytes);
        }
        response.addCookie(new Cookie("auth", data));
    }
}
