
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_long_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = 5L;
        CWE563_Unused_Variable__unused_value_long_81_base baseObject = new CWE563_Unused_Variable__unused_value_long_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 5L;
        IO.writeLine("" + data);
        CWE563_Unused_Variable__unused_value_long_81_base baseObject = new CWE563_Unused_Variable__unused_value_long_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = 5L;
        CWE563_Unused_Variable__unused_value_long_81_base baseObject = new CWE563_Unused_Variable__unused_value_long_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
