
package testcases.CWE572_Call_to_Thread_run_Instead_of_start;
import testcasesupport.*;
public class CWE572_Call_to_Thread_run_Instead_of_start__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
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
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
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
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
