
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE190_Integer_Overflow__int_getCookies_Servlet_square_73a extends AbstractTestCaseServlet
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
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE190_Integer_Overflow__int_getCookies_Servlet_square_73b()).badSink(dataLinkedList , request, response );
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
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE190_Integer_Overflow__int_getCookies_Servlet_square_73b()).goodG2BSink(dataLinkedList , request, response );
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
        LinkedList<Integer> dataLinkedList = new LinkedList<Integer>();
        dataLinkedList.add(0, data);
        dataLinkedList.add(1, data);
        dataLinkedList.add(2, data);
        (new CWE190_Integer_Overflow__int_getCookies_Servlet_square_73b()).goodB2GSink(dataLinkedList , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
