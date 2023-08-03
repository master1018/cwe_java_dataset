
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_short_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int data;
        data = Short.MAX_VALUE + 5;
        return data;
    }
    public void bad() throws Throwable
    {
        int data = badSource();
        {
            IO.writeLine((short)data);
        }
    }
    private int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        int data = goodG2BSource();
        {
            IO.writeLine((short)data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
