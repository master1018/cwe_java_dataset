
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE191_Integer_Underflow__int_getParameter_Servlet_sub_41 extends AbstractTestCaseServlet
{
    private void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            String stringNumber = request.getParameter("name");
            try
            {
                data = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception reading data from parameter 'name'", exceptNumberFormat);
            }
        }
        badSink(data , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        goodG2BSink(data , request, response );
    }
    private void goodB2GSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data > Integer.MIN_VALUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            String stringNumber = request.getParameter("name");
            try
            {
                data = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception reading data from parameter 'name'", exceptNumberFormat);
            }
        }
        goodB2GSink(data , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
