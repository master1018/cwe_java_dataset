
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_StringBuilder_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        while (true)
        {
            ; 
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        while (true)
        {
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
