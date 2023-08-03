
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_max_value_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        badPrivate = true;
        badSink(count );
    }
    private void badSink(int count ) throws Throwable
    {
        if (badPrivate)
        {
            Thread.sleep(count);
        }
    }
    private boolean goodB2G1Private = false;
    private boolean goodB2G2Private = false;
    private boolean goodG2BPrivate = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        goodB2G1Private = false;
        goodB2G1Sink(count );
    }
    private void goodB2G1Sink(int count ) throws Throwable
    {
        if (goodB2G1Private)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        goodB2G2Private = true;
        goodB2G2Sink(count );
    }
    private void goodB2G2Sink(int count ) throws Throwable
    {
        if (goodB2G2Private)
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        goodG2BPrivate = true;
        goodG2BSink(count );
    }
    private void goodG2BSink(int count ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            Thread.sleep(count);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
