
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getParameter_Servlet_write_54a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = Integer.MIN_VALUE; 
        {
            String stringNumber = request.getParameter("name");
            try
            {
                count = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception reading count from parameter 'name'", exceptNumberFormat);
            }
        }
        (new CWE400_Resource_Exhaustion__getParameter_Servlet_write_54b()).badSink(count , request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = 2;
        (new CWE400_Resource_Exhaustion__getParameter_Servlet_write_54b()).goodG2BSink(count , request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = Integer.MIN_VALUE; 
        {
            String stringNumber = request.getParameter("name");
            try
            {
                count = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception reading count from parameter 'name'", exceptNumberFormat);
            }
        }
        (new CWE400_Resource_Exhaustion__getParameter_Servlet_write_54b()).goodB2GSink(count , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
