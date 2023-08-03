
package testcases.CWE383_Direct_Use_of_Threads;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE383_Direct_Use_of_Threads__Servlet_11 extends AbstractTestCaseServletBadOnly
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    try
                    {
                        Thread.sleep(10000); 
                    }
                    catch (InterruptedException exceptInterrupted)
                    {
                        IO.writeLine("InterruptedException");
                    }
                }
            };
            Thread threadOne = new Thread(runnable);
            threadOne.start();
            while(true)
            {
                if (!threadOne.isAlive())
                {
                    break;
                }
                Thread.sleep(1000);
            }
            response.getWriter().write("thread is done!");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
