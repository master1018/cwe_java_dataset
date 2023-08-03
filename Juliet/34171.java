
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__max_value_for_loop_41 extends AbstractTestCase
{
    private void badSink(int count ) throws Throwable
    {
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void bad() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        badSink(count  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(int count ) throws Throwable
    {
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        goodG2BSink(count  );
    }
    private void goodB2GSink(int count ) throws Throwable
    {
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        goodB2GSink(count  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
