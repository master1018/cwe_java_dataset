
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_square_05 extends AbstractTestCaseServlet
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = Integer.MIN_VALUE; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        try
                        {
                            data = Integer.parseInt(token.substring(3)); 
                        }
                        catch(NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                        }
                        break; 
                    }
                }
            }
        }
        else
        {
            data = 0;
        }
        if (privateTrue)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (privateFalse)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateTrue)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateTrue)
        {
            int result = (int)(data * data);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = Integer.MIN_VALUE; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        try
                        {
                            data = Integer.parseInt(token.substring(3)); 
                        }
                        catch(NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                        }
                        break; 
                    }
                }
            }
        }
        else
        {
            data = 0;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
            }
        }
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = Integer.MIN_VALUE; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        try
                        {
                            data = Integer.parseInt(token.substring(3)); 
                        }
                        catch(NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                        }
                        break; 
                    }
                }
            }
        }
        else
        {
            data = 0;
        }
        if (privateTrue)
        {
            if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Integer.MAX_VALUE)))
            {
                int result = (int)(data * data);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform squaring.");
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