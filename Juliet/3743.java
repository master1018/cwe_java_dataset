
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_value_String_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = "Good";
        CWE563_Unused_Variable__unused_value_String_81_base baseObject = new CWE563_Unused_Variable__unused_value_String_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "Good";
        IO.writeLine(data);
        CWE563_Unused_Variable__unused_value_String_81_base baseObject = new CWE563_Unused_Variable__unused_value_String_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = "Good";
        CWE563_Unused_Variable__unused_value_String_81_base baseObject = new CWE563_Unused_Variable__unused_value_String_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
