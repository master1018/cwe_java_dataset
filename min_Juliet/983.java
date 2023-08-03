
package testcases.CWE585_Empty_Sync_Block;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE585_Empty_Sync_Block__Thread_01 extends AbstractTestCase 
{
    static private int intBad = 1;
    static public void helperBad()
    {
        synchronized(CWE585_Empty_Sync_Block__Thread_01.class) 
        {
        }
        intBad = intBad * 2;
    }
    public void bad() throws InterruptedException 
    {
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE585_Empty_Sync_Block__Thread_01.helperBad(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE585_Empty_Sync_Block__Thread_01.helperBad(); 
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        IO.writeLine(intBad);  
    }
    static private int intGood1 = 1;
    static public void helperGood1() 
    {
        synchronized(CWE585_Empty_Sync_Block__Thread_01.class) 
        { 
            intGood1 = intGood1 * 2;
        }
    }
    private void good1() throws InterruptedException
    {   
        Thread threadOne = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE585_Empty_Sync_Block__Thread_01.helperGood1(); 
            }
        });
        Thread threadTwo = new Thread(new Runnable() 
        {
            public void run() 
            { 
                CWE585_Empty_Sync_Block__Thread_01.helperGood1(); 
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
