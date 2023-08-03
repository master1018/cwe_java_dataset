
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_int_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 5;
        CWE563_Unused_Variable__unused_value_int_81_base baseObject = new CWE563_Unused_Variable__unused_value_int_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 5;
        IO.writeLine("" + data);
        CWE563_Unused_Variable__unused_value_int_81_base baseObject = new CWE563_Unused_Variable__unused_value_int_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 5;
        CWE563_Unused_Variable__unused_value_int_81_base baseObject = new CWE563_Unused_Variable__unused_value_int_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
