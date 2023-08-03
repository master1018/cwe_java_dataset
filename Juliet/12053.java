
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getParameter_Servlet_for_loop_45 extends AbstractTestCaseServlet
{
    private int countBad;
    private int countGoodG2B;
    private int countGoodB2G;
    private void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countBad;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
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
        countBad = count;
        badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countGoodG2B;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = 2;
        countGoodG2B = count;
        goodG2BSink(request, response);
    }
    private void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countGoodB2G;
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
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
        countGoodB2G = count;
        goodB2GSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
