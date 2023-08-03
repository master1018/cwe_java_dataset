
package testcases.CWE833_Deadlock;
import java.util.logging.Level;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE833_Deadlock__synchronized_Objects_Thread_01 extends AbstractTestCase 
{
    static private int intBadNumber1 = 3;
    static private final Object BAD_NUMBER1_LOCK = new Object();
    static private int intBadNumber2 = 5;
    static private final Object BAD_NUMBER2_LOCK = new Object();
    static public void helperAddBad()
    {
        synchronized(BAD_NUMBER1_LOCK) 
        {
            try 
            { 
                Thread.sleep(1000); 
            } 
            catch (InterruptedException exceptInterrupted) 
            {
                IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
            }
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
            try 
            { 
                Thread.sleep(1000); 
            } 
            catch (InterruptedException exceptInterrupted) 
            {
                IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
            }
            synchronized(BAD_NUMBER1_LOCK) 
            {
                intBadNumber1 = intBadNumber1 * intBadNumber2;
            }
        }
    }
    public void bad() throws Throwable 
    {
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__synchronized_Objects_Thread_01.helperAddBad(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__synchronized_Objects_Thread_01.helperMultiplyBad(); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        IO.writeLine(intBadNumber1);
    }
    static private int intGood1Number1 = 3;
    static private final Object GOOD_NUMBER1_LOCK = new Object();
    static private int intGood1Number2 = 5;
    static private final Object GOOD_NUMBER2_LOCK = new Object();
    static public void helperAddGood1()
    {
        synchronized(GOOD_NUMBER1_LOCK) 
        {
            try 
            { 
                Thread.sleep(1000); 
            } 
            catch (InterruptedException exceptInterrupted) 
            {
                IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
            }
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
            try 
            { 
                Thread.sleep(1000); 
            } 
            catch (InterruptedException exceptInterrupted) 
            {
                IO.logger.log(Level.WARNING, "Sleep Interrupted", exceptInterrupted);
            }
            synchronized(GOOD_NUMBER2_LOCK) 
            {
                intGood1Number1 = intGood1Number1 * intGood1Number2;
            }
        }
    }
    public void good1() throws Throwable 
    {
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__synchronized_Objects_Thread_01.helperAddGood1(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE833_Deadlock__synchronized_Objects_Thread_01.helperMultiplyGood1(); 
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
