
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_int_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 5;
        CWE563_Unused_Variable__unused_init_variable_int_81_base baseObject = new CWE563_Unused_Variable__unused_init_variable_int_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodB2G();
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 5;
        CWE563_Unused_Variable__unused_init_variable_int_81_base baseObject = new CWE563_Unused_Variable__unused_init_variable_int_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
