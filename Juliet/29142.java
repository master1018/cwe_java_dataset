
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_String_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        ; 
        if (IO.staticTrue)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        ; 
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            data = "Good";
            IO.writeLine(data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        ; 
        if (IO.staticTrue)
        {
            data = "Good";
            IO.writeLine(data);
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
