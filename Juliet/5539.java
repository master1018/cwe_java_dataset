
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE191_Integer_Underflow__int_getCookies_Servlet_multiply_15 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
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
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        switch (5)
        {
        case 6:
            data = 0;
            break;
        default:
            data = 2;
            break;
        }
        switch (7)
        {
        case 7:
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = 2;
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
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
        default:
            data = 0;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
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
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
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
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
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
        default:
            IO.writeLine("Benign, fixed string");
            break;
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