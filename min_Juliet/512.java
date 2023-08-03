
package testcases.CWE765_Multiple_Unlocks;
import java.util.concurrent.locks.ReentrantLock;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE765_Multiple_Unlocks__ReentrantLock_Thread_01 extends AbstractTestCase 
{
    static private int intBad = 1;
    static private final ReentrantLock REENTRANT_LOCK_BAD = new ReentrantLock();
    static public void helperBad() 
    {
        REENTRANT_LOCK_BAD.lock(); 
        }
    }
    public void bad() throws Throwable 
    {
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE765_Multiple_Unlocks__ReentrantLock_Thread_01.helperBad(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE765_Multiple_Unlocks__ReentrantLock_Thread_01.helperBad(); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        IO.writeLine(intBad);  
    }
    static private int intGood1 = 1;
    static private final ReentrantLock REENTRANT_LOCK_GOOD1 = new ReentrantLock();
    static public void helperGood1() 
    {
        REENTRANT_LOCK_GOOD1.lock(); 
        }
    }
    private void good1() throws InterruptedException
    {        
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE765_Multiple_Unlocks__ReentrantLock_Thread_01.helperGood1(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE765_Multiple_Unlocks__ReentrantLock_Thread_01.helperGood1(); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        IO.writeLine(intGood1);
    }  
    public void good() throws InterruptedException 
    {
        good1();
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
