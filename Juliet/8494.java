
package testcases.CWE572_Call_to_Thread_run_Instead_of_start;
import testcasesupport.*;
public class CWE572_Call_to_Thread_run_Instead_of_start__basic_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            IO.writeLine("bad() Main thread name is: " + Thread.currentThread().getName());
            Thread threadOne = new Thread()
            {
                public void run()
                {
                    IO.writeLine("bad() In thread: " + Thread.currentThread().getName());
                }
            };
            threadOne.run(); 
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            IO.writeLine("good() Main thread name is: " + Thread.currentThread().getName());
            Thread threadTwo = new Thread()
            {
                public void run()
                {
                    IO.writeLine("good() In thread: " + Thread.currentThread().getName());
                }
            };
            threadTwo.start(); 
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
