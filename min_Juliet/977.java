
package testcases.CWE585_Empty_Sync_Block;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import testcasesupport.AbstractTestCaseServlet;
public class CWE585_Empty_Sync_Block__Servlet_01 extends AbstractTestCaseServlet 
{
    private static final long serialVersionUID = 1L;
    static private int intBad = 1;
    static public void helperBad()
    {
        synchronized(CWE585_Empty_Sync_Block__Servlet_01.class) 
        {
        }
        intBad = intBad * 2;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException 
    {
        helperBad();
        response.getWriter().write(intBad);
    }
    static private int intGood1 = 1;
    static public void helperGood1() 
    {
        synchronized(CWE585_Empty_Sync_Block__Servlet_01.class) 
        { 
            intGood1 = intGood1 * 2;
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException
    {        
        helperGood1();
        response.getWriter().write(intGood1);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException 
    {
        good1(request, response);
    } 
}
