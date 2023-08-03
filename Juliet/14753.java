
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_String_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "Good";
        if (IO.staticFive == 5)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        data = "Good";
        if (IO.staticFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine(data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        data = "Good";
        if (IO.staticFive == 5)
        {
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
