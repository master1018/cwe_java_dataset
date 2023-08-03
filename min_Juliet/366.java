
package testcases.CWE600_Uncaught_Exception_in_Servlet;
import java.util.logging.Level;
import java.io.IOException;
import java.net.InetAddress;
import javax.servlet.http.*;
import testcasesupport.*;
public class CWE600_Uncaught_Exception_in_Servlet__getParameter_01 extends AbstractTestCaseServlet 
{
    private static final long serialVersionUID = 1L; 
    public void bad(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {        
        String ipAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        if (inetAddress == null)
        {
            IO.logger.log(Level.WARNING, "Problem getting IP address");
        }
        else
        {
            IO.writeLine("IP= " + inetAddress.getAddress().toString());
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) 
    {
        try 
        {
            String ipAddress = request.getRemoteAddr();
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            if (inetAddress == null)
            {
                IO.logger.log(Level.WARNING, "Problem getting IP address");
            }
            else
            {
                IO.writeLine("IP= " + inetAddress.getAddress().toString());
            }
        }
        catch (IOException exceptIO) 
        {
            IO.logger.log(Level.WARNING, "Problem getting IP address");
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) 
    {
        good1(request, response);
    }
}
