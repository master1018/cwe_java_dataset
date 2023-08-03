
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE470_Unsafe_Reflection__getQueryString_Servlet_31 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String dataCopy;
        {
            String data;
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
            dataCopy = data;
        }
        {
            String data = dataCopy;
            Class<?> tempClass = Class.forName(data);
            Object tempClassObject = tempClass.newInstance();
            IO.writeLine(tempClassObject.toString()); 
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = "Testing.test";
            dataCopy = data;
        }
        {
            String data = dataCopy;
            Class<?> tempClass = Class.forName(data);
            Object tempClassObject = tempClass.newInstance();
            IO.writeLine(tempClassObject.toString()); 
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
