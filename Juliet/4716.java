
package testcases.CWE833_Deadlock;
import java.util.logging.Level;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE833_Deadlock__synchronized_methods_Thread_01 extends AbstractTestCase 
{
    public synchronized void helperBowBad(CWE833_Deadlock__synchronized_methods_Thread_01 bower) 
    {
        IO.writeLine("helperBowBad");
        try 
        { 
            Thread.sleep(1000); 
        } 
        catch (InterruptedException exceptInterrupted) 
        {
            IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
        }
        bower.helperBowBackBad(this); 
    }
    public synchronized void helperBowBackBad(CWE833_Deadlock__synchronized_methods_Thread_01 bower) 
    {
        IO.writeLine("helperBowBackBad");
    }
    public void bad() throws InterruptedException 
    {
        final CWE833_Deadlock__synchronized_methods_Thread_01 FINAL_THREAD_ONE = new CWE833_Deadlock__synchronized_methods_Thread_01();
        final CWE833_Deadlock__synchronized_methods_Thread_01 FINAL_THREAD_TWO = new CWE833_Deadlock__synchronized_methods_Thread_01();
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                FINAL_THREAD_ONE.helperBowBad(FINAL_THREAD_TWO); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                FINAL_THREAD_TWO.helperBowBad(FINAL_THREAD_ONE); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
    public void helperBowGood1(CWE833_Deadlock__synchronized_methods_Thread_01 bower) 
    {
        synchronized(this) 
        {
            IO.writeLine("helperBowGood1");
            try 
            { 
                Thread.sleep(1000); 
            } 
            catch (InterruptedException exceptInterrupted) 
            {
                IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
            }
        }
        bower.helperBowBackGood1(this); 
    }
    public synchronized void helperBowBackGood1(CWE833_Deadlock__synchronized_methods_Thread_01 bower) 
    {
        IO.writeLine("helperBowBackGood1");
    }
    private void good1()  throws InterruptedException 
    {
        final CWE833_Deadlock__synchronized_methods_Thread_01 FINAL_THREAD_ONE = new CWE833_Deadlock__synchronized_methods_Thread_01();
        final CWE833_Deadlock__synchronized_methods_Thread_01 FINAL_THREAD_TWO = new CWE833_Deadlock__synchronized_methods_Thread_01();
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                FINAL_THREAD_ONE.helperBowGood1(FINAL_THREAD_TWO); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                FINAL_THREAD_TWO.helperBowGood1(FINAL_THREAD_ONE); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
    public void good() throws Throwable 
    {
        good1();
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
