
package testcases.CWE579_Non_Serializable_in_Session;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import testcasesupport.AbstractTestCaseServlet;
public class CWE579_Non_Serializable_in_Session__Servlet_01 extends AbstractTestCaseServlet 
{
    private static final long serialVersionUID = 1L;
    static class BadObject 
    {
        public String badString = "Bad";
    }
    static class GoodObject implements Serializable 
    {
        private static final long serialVersionUID = 7925935052619185041L;    
        public String goodString = "Good";
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        BadObject badObject = new BadObject();
        request.getSession(true).setAttribute("BadObject", badObject);
        response.getWriter().println(((BadObject)request.getSession().getAttribute("BadObject")).badString);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        GoodObject goodObject = new GoodObject();
        request.getSession(true).setAttribute("GoodObject", goodObject);
        response.getWriter().println(((GoodObject)request.getSession().getAttribute("GoodObject")).goodString);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        good1(request, response);
    }
}
