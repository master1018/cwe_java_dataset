
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getQueryString_Servlet_write_73a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = Integer.MIN_VALUE; 
        {
            StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken(); 
                if(token.startsWith("id=")) 
                {
                    try
                    {
                        count = Integer.parseInt(token.substring(3)); 
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                    }
                    break; 
                }
            }
        }
        LinkedList<Integer> countLinkedList = new LinkedList<Integer>();
        countLinkedList.add(0, count);
        countLinkedList.add(1, count);
        countLinkedList.add(2, count);
        (new CWE400_Resource_Exhaustion__getQueryString_Servlet_write_73b()).badSink(countLinkedList , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = 2;
        LinkedList<Integer> countLinkedList = new LinkedList<Integer>();
        countLinkedList.add(0, count);
        countLinkedList.add(1, count);
        countLinkedList.add(2, count);
        (new CWE400_Resource_Exhaustion__getQueryString_Servlet_write_73b()).goodG2BSink(countLinkedList , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        count = Integer.MIN_VALUE; 
        {
            StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken(); 
                if(token.startsWith("id=")) 
                {
                    try
                    {
                        count = Integer.parseInt(token.substring(3)); 
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                    }
                    break; 
                }
            }
        }
        LinkedList<Integer> countLinkedList = new LinkedList<Integer>();
        countLinkedList.add(0, count);
        countLinkedList.add(1, count);
        countLinkedList.add(2, count);
        (new CWE400_Resource_Exhaustion__getQueryString_Servlet_write_73b()).goodB2GSink(countLinkedList , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
