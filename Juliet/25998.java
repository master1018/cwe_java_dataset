
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_StringBuilder_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        ; 
        while (true)
        {
            ; 
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        ; 
        while (true)
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
            break;
        }
    }
    public void good() throws Throwable
    {
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
