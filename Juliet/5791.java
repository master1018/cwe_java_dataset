
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
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
        CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81_base baseObject = new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81_bad();
        baseObject.action(data , request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81_base baseObject = new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81_goodG2B();
        baseObject.action(data , request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
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
        CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81_base baseObject = new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_check_min_81_goodB2G();
        baseObject.action(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
