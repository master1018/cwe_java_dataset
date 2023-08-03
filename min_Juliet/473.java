
package testcases.CWE609_Double_Checked_Locking;
import java.util.concurrent.locks.ReentrantLock;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE609_Double_Checked_Locking__Thread_01 extends AbstractTestCase 
{
    private static String stringBad = null;
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperBad()); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperBad()); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
    private volatile static String stringGood1 = null; 
    public static String helperGood1() 
    { 
        if (stringGood1 == null)
        {
            synchronized(CWE609_Double_Checked_Locking__Thread_01.class)
            {
                if (stringGood1 == null)
                {
                    stringGood1 = "stringGood1";
                }
            }
        }
        return stringGood1;
    }
    public void good1() throws InterruptedException
    { 
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood1()); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood1()); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
    private static String stringGood2 = null;
    public synchronized static String helperGood2() 
    { 
        if (stringGood2 == null)
        {
            stringGood2 = "stringGood2";
        }
        return stringGood2;
    }
    public void good2() throws InterruptedException
    { 
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood2()); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood2()); 
            }
        });
    threadOne.start();
    threadTwo.start();
    threadOne.join();
    threadTwo.join();
    }
    private static String stringGood3 = null; 
    public static String helperGood3() 
    { 
        synchronized(CWE609_Double_Checked_Locking__Thread_01.class) 
        { 
            if (stringGood3 == null)
            {
                stringGood3 = "stringGood3";
            }
        return stringGood3;
        }
    }
    public void good3() throws InterruptedException
    { 
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood3()); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood3()); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
    private static String stringGood4 = null; 
    static private final Object stringGood4Lock = new Object();
    public static String helperGood4() 
    { 
        synchronized(stringGood4Lock) 
        { 
            if (stringGood4 == null)
            {
                stringGood4 = "stringGood4";
            }
          return stringGood4;
        }
    }
    public void good4() throws InterruptedException
    { 
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood4()); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood4()); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
    private static String stringGood5 = null; 
    static private final ReentrantLock good5ReentrantLock = new ReentrantLock();
    public static String helperGood5() 
    { 
        good5ReentrantLock.lock(); 
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood5()); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                IO.writeLine(CWE609_Double_Checked_Locking__Thread_01.helperGood5()); 
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
        good2();
        good3();
        good4();
        good5();
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
