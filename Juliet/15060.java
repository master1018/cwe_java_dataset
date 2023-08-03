
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_long_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        ; 
        if (IO.staticTrue)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        ; 
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            data = 5L;
            IO.writeLine("" + data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        long data;
        ; 
        if (IO.staticTrue)
        {
            data = 5L;
            IO.writeLine("" + data);
        }
    }
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
