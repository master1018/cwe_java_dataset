
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_square_42 extends AbstractTestCaseServlet
{
    private int badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
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
        return data;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = badSource(request, response);
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    private int goodG2BSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = goodG2BSource(request, response);
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
    private int goodB2GSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
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
        return data;
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = goodB2GSource(request, response);
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
