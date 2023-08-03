
package testcases.CWE833_Deadlock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE833_Deadlock__ReentrantLock_Thread_01 extends AbstractTestCase 
{
    static private int intBadNumber1 = 3;
    static private final ReentrantLock BAD_NUMBER1_REENTRANTLOCK = new ReentrantLock();
    static private int intBadNumber2 = 5;
    static private final ReentrantLock BAD_NUMBER2_REENTRANTLOCK = new ReentrantLock();
    static public void helperAddBad()
    {
        BAD_NUMBER1_REENTRANTLOCK.lock();
        try 
        { 
            Thread.sleep(1000); 
        } 
        catch (InterruptedException exceptInterrupted) 
        {
            IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
        }
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
        try 
        { 
            Thread.sleep(1000); 
        } 
        catch (InterruptedException exceptInterrupted) 
        {
            IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
        }
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
    public void bad() throws Throwable 
    {
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__ReentrantLock_Thread_01.helperAddBad(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__ReentrantLock_Thread_01.helperMultiplyBad(); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        IO.writeLine(intBadNumber1);
    }
    static private int intGood1Number1 = 3;
    static private final ReentrantLock GOOD1_NUMBER1_REENTRANTLOCK = new ReentrantLock();
    static private int intGood1Number2 = 5;
    static private final ReentrantLock GOOD1_NUMBER2_REENTRANTLOCK = new ReentrantLock();
    static public void helperAddGood1()
    {
        GOOD1_NUMBER1_REENTRANTLOCK.lock();
        try 
        { 
            Thread.sleep(1000); 
        } 
        catch (InterruptedException exceptInterrupted) 
        {
            IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
        }
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
        try 
        { 
            Thread.sleep(1000); 
        } 
        catch (InterruptedException exceptInterrupted) 
        {
            IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
        }
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
    public void good1() throws Throwable 
    {
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__ReentrantLock_Thread_01.helperAddGood1(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__ReentrantLock_Thread_01.helperMultiplyGood1(); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        IO.writeLine(intGood1Number1);
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
