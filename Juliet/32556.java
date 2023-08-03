
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int [] data;
        data = null;
        CWE476_NULL_Pointer_Dereference__int_array_81_base baseObject = new CWE476_NULL_Pointer_Dereference__int_array_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int [] data;
        data = new int[5];
        CWE476_NULL_Pointer_Dereference__int_array_81_base baseObject = new CWE476_NULL_Pointer_Dereference__int_array_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        int [] data;
        data = null;
        CWE476_NULL_Pointer_Dereference__int_array_81_base baseObject = new CWE476_NULL_Pointer_Dereference__int_array_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
