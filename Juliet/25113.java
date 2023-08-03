
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_int_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 5;
        if (IO.STATIC_FINAL_TRUE)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        data = 5;
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine("" + data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        data = 5;
        if (IO.STATIC_FINAL_TRUE)
        {
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
