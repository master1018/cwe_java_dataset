
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE470_Unsafe_Reflection__getQueryString_Servlet_21 extends AbstractTestCaseServlet
{
    private boolean badPrivate = false;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        badPrivate = true;
        data = bad_source(request, response);
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private String bad_source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        if (badPrivate)
        {
            data = ""; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        data = token.substring(3); 
                        break; 
                    }
                }
            }
        }
        else
        {
            data = null;
        }
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B1_private = false;
        data = goodG2B1_source(request, response);
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private String goodG2B1_source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        if (goodG2B1_private)
        {
            data = null;
        }
        else
        {
            data = "Testing.test";
        }
        return data;
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        goodG2B2_private = true;
        data = goodG2B2_source(request, response);
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private String goodG2B2_source(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        if (goodG2B2_private)
        {
            data = "Testing.test";
        }
        else
        {
            data = null;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
