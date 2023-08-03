
package testcases.CWE80_XSS.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_PropertiesFile_52b
{
    public void badSink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        (new CWE80_XSS__Servlet_PropertiesFile_52c()).badSink(data , request, response);
    }
    public void goodG2BSink(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        (new CWE80_XSS__Servlet_PropertiesFile_52c()).goodG2BSink(data , request, response);
    }
}
