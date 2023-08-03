
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_getParameter_Servlet_divide_21 extends AbstractTestCaseServlet
{
    private boolean badPrivate = false;
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
        badPrivate = true;
        badSink(data , request, response);
    }
    private void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (badPrivate)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private boolean goodB2G1Private = false;
    private boolean goodB2G2Private = false;
    private boolean goodG2BPrivate = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodB2G1(request, response);
        goodB2G2(request, response);
        goodG2B(request, response);
    }
    private void goodB2G1(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        goodB2G1Private = false;
        goodB2G1Sink(data , request, response);
    }
    private void goodB2G1Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (goodB2G1Private)
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
        goodB2G2Private = true;
        goodB2G2Sink(data , request, response);
    }
    private void goodB2G2Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (goodB2G2Private)
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
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        goodG2BPrivate = true;
        goodG2BSink(data , request, response);
    }
    private void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (goodG2BPrivate)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
