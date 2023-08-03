
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_int_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        int data;
        data = 5;
        if (privateTrue)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        data = 5;
        if (privateFalse)
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
        if (privateTrue)
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
