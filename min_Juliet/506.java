
package testcases.CWE833_Deadlock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import testcasesupport.AbstractTestCaseServlet;
public class CWE833_Deadlock__synchronized_Objects_Servlet_01 extends AbstractTestCaseServlet 
{
    private static final long serialVersionUID = 1L;
    static private int intBadNumber1 = 3;
    static private final Object BAD_NUMBER1_LOCK = new Object();
    static private int intBadNumber2 = 5;
    static private final Object BAD_NUMBER2_LOCK = new Object();
    static public void helperAddBad()
    {
        synchronized(BAD_NUMBER1_LOCK) 
        {
            synchronized(BAD_NUMBER2_LOCK) 
            {
                intBadNumber1 = intBadNumber1 + intBadNumber2;
            }
        }
    }
    static public void helperMultiplyBad()
    {
        synchronized(BAD_NUMBER2_LOCK) 
        { 
            synchronized(BAD_NUMBER1_LOCK) 
            {
                intBadNumber1 = intBadNumber1 * intBadNumber2;
            }
        }
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable 
    {
        if(request.isRequestedSessionIdValid()) 
        {
            helperAddBad();
        } 
        else 
        {
            helperMultiplyBad();
        }
        response.getWriter().write(intBadNumber1);
    }
    static private int intGood1Number1 = 3;
    static private final Object GOOD_NUMBER1_LOCK = new Object();
    static private int intGood1Number2 = 5;
    static private final Object GOOD_NUMBER2_LOCK = new Object();
    static public void helperAddGood1()
    {
        synchronized(GOOD_NUMBER1_LOCK) 
        {
            synchronized(GOOD_NUMBER2_LOCK) 
            {
                intGood1Number1 = intGood1Number1 + intGood1Number2;
            }
        }
    }
    static public void helperMultiplyGood1()
    {
        synchronized(GOOD_NUMBER1_LOCK) 
        { 
            synchronized(GOOD_NUMBER2_LOCK) 
            {
                intGood1Number1 = intGood1Number1 * intGood1Number2;
            }
        }
    }
    public void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable 
    {
        if(request.isRequestedSessionIdValid()) 
        {
            helperAddGood1();
        } 
        else 
        {
            helperMultiplyGood1();
        }
        response.getWriter().write(intGood1Number1);
    }  
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable 
    {
        good1(request, response);
    } 
}
