
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getParameter_Servlet_write_61b
{
    public int badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        return count;
    }
    public int goodG2BSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = 2;
        return count;
    }
    public int goodB2GSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        return count;
    }
}
