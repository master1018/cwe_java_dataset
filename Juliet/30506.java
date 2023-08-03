
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__PropertiesFile_setHeaderServlet_74b
{
    public void badSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            response.setHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    public void goodG2BSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            response.setHeader("Location", "/author.jsp?lang=" + data);
        }
    }
    public void goodB2GSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            data = URLEncoder.encode(data, "UTF-8");
            response.setHeader("Location", "/author.jsp?lang=" + data);
        }
    }
}
