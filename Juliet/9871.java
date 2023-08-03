
package testcases.CWE315_Plaintext_Storage_in_Cookie;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.MessageDigest;
import java.net.PasswordAuthentication;
public class CWE315_Plaintext_Storage_in_Cookie__Servlet_66a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "BP@ssw0rd".toCharArray());
        data = credentials.getUserName() + ":" + (new String(credentials.getPassword()));
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE315_Plaintext_Storage_in_Cookie__Servlet_66b()).badSink(dataArray , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "GP@ssw0rd".toCharArray());
        {
            String salt = "ThisIsMySalt";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            String credentialsToHash = credentials.getUserName() + ":" + (new String(credentials.getPassword()));
            byte[] hashedCredsAsBytes = messageDigest.digest((salt+credentialsToHash).getBytes("UTF-8"));
            data = IO.toHex(hashedCredsAsBytes);
        }
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE315_Plaintext_Storage_in_Cookie__Servlet_66b()).goodG2BSink(dataArray , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "BP@ssw0rd".toCharArray());
        data = credentials.getUserName() + ":" + (new String(credentials.getPassword()));
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE315_Plaintext_Storage_in_Cookie__Servlet_66b()).goodB2GSink(dataArray , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
