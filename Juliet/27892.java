
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashMap_22b
{
    public int badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashMap_22a.badPublicStatic)
        {
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
        if (CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashMap_22a.goodG2B1PublicStatic)
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
        if (CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashMap_22a.goodG2B2PublicStatic)
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
