
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_StringBuilder_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        CWE563_Unused_Variable__unused_value_StringBuilder_81_base baseObject = new CWE563_Unused_Variable__unused_value_StringBuilder_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        IO.writeLine(data.toString());
        CWE563_Unused_Variable__unused_value_StringBuilder_81_base baseObject = new CWE563_Unused_Variable__unused_value_StringBuilder_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        CWE563_Unused_Variable__unused_value_StringBuilder_81_base baseObject = new CWE563_Unused_Variable__unused_value_StringBuilder_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
