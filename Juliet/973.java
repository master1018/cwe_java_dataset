
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_getParameter_Servlet_divide_13 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE==5)
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
        if (IO.STATIC_FINAL_FIVE==5)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE==5)
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
        if (IO.STATIC_FINAL_FIVE!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != 0)
            {
                IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
            }
        }
    }
    private void goodB2G2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE==5)
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
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if (data != 0)
            {
                IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
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
