
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_45 extends AbstractTestCaseServlet
{
    private int dataBad;
    private int dataGoodG2B;
    private int dataGoodB2G;
    private void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataBad;
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        dataBad = data;
        badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataGoodG2B;
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        dataGoodG2B = data;
        goodG2BSink(request, response);
    }
    private void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataGoodB2G;
        if(data > 0) 
        {
            if (data < (Integer.MAX_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        dataGoodB2G = data;
        goodB2GSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
