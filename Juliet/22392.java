
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE191_Integer_Underflow__int_getCookies_Servlet_sub_09 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_TRUE)
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
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FALSE)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_TRUE)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_TRUE)
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
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
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
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_TRUE)
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
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_TRUE)
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
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
        goodB2G1(request, response);
        goodB2G2(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
