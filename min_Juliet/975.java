
package testcases.CWE833_Deadlock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import testcasesupport.AbstractTestCaseServlet;
public class CWE833_Deadlock__ReentrantLock_Servlet_01 extends AbstractTestCaseServlet 
{
    private static final long serialVersionUID = 1L;
    static private int intBadNumber1 = 3;
    static private final ReentrantLock BAD_NUMBER1_REENTRANTLOCK = new ReentrantLock();
    static private int intBadNumber2 = 5;
    static private final ReentrantLock BAD_NUMBER2_REENTRANTLOCK = new ReentrantLock();
    static public void helperAddBad()
    {
        BAD_NUMBER1_REENTRANTLOCK.lock();
        BAD_NUMBER2_REENTRANTLOCK.lock();
        try 
        {
            intBadNumber1 = intBadNumber1 + intBadNumber2;
        } 
        finally 
        {
            BAD_NUMBER2_REENTRANTLOCK.unlock();
            BAD_NUMBER1_REENTRANTLOCK.unlock();
        }
    }
    static public void helperMultiplyBad()
    {
        BAD_NUMBER2_REENTRANTLOCK.lock(); 
        BAD_NUMBER1_REENTRANTLOCK.lock();
        try 
        {
            intBadNumber1 = intBadNumber1 * intBadNumber2;
        } 
        finally 
        {
            BAD_NUMBER1_REENTRANTLOCK.unlock();
            BAD_NUMBER2_REENTRANTLOCK.unlock();
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
    static private final ReentrantLock GOOD1_NUMBER1_REENTRANTLOCK = new ReentrantLock();
    static private int intGood1Number2 = 5;
    static private final ReentrantLock GOOD1_NUMBER2_REENTRANTLOCK = new ReentrantLock();
    static public void helperAddGood1()
    {
        GOOD1_NUMBER1_REENTRANTLOCK.lock();
        GOOD1_NUMBER2_REENTRANTLOCK.lock();
        try 
        {
            intGood1Number1 = intGood1Number1 + intGood1Number2;
        } 
        finally 
        {
            GOOD1_NUMBER2_REENTRANTLOCK.unlock();
            GOOD1_NUMBER1_REENTRANTLOCK.unlock();
        }
    }
    static public void helperMultiplyGood1()
    {
        GOOD1_NUMBER1_REENTRANTLOCK.lock(); 
        GOOD1_NUMBER2_REENTRANTLOCK.lock();
        try 
        {
            intGood1Number1 = intGood1Number1 * intGood1Number2;
        } 
        finally 
        {
            GOOD1_NUMBER2_REENTRANTLOCK.unlock();
            GOOD1_NUMBER1_REENTRANTLOCK.unlock();
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
