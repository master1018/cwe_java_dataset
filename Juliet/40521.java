
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_long_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        ; 
        switch (7)
        {
        case 7:
            ; 
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        ; 
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            data = 5L;
            IO.writeLine("" + data);
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        long data;
        ; 
        switch (7)
        {
        case 7:
            data = 5L;
            IO.writeLine("" + data);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
