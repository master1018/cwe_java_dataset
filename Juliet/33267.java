
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE191_Integer_Underflow__int_getCookies_Servlet_multiply_16 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        while (true)
        {
            data = Integer.MIN_VALUE; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    String stringNumber = cookieSources[0].getValue();
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading data from cookie", exceptNumberFormat);
                    }
                }
            }
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        while (true)
        {
            data = 2;
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        while (true)
        {
            data = Integer.MIN_VALUE; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    String stringNumber = cookieSources[0].getValue();
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading data from cookie", exceptNumberFormat);
                    }
                }
            }
            break;
        }
        while (true)
        {
            if(data < 0) 
            {
                if (data > (Integer.MIN_VALUE/2))
                {
                    int result = (int)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
            break;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
