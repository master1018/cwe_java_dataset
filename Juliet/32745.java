
package testcases.CWE315_Plaintext_Storage_in_Cookie;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.MessageDigest;
public class CWE315_Plaintext_Storage_in_Cookie__Servlet_66b
{
    public void badSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        response.addCookie(new Cookie("auth", data));
    }
    public void goodG2BSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        response.addCookie(new Cookie("auth", data));
    }
    public void goodB2GSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
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
