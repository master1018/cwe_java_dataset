
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_22b
{
    public int badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_22a.badPublicStatic)
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
        return data;
    }
    public int goodG2B1Source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_22a.goodG2B1PublicStatic)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    public int goodG2B2Source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_22a.goodG2B2PublicStatic)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
}
