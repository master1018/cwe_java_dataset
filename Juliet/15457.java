
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = null;
        CWE476_NULL_Pointer_Dereference__String_81_base baseObject = new CWE476_NULL_Pointer_Dereference__String_81_bad();
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
        data = "This is not null";
        CWE476_NULL_Pointer_Dereference__String_81_base baseObject = new CWE476_NULL_Pointer_Dereference__String_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = null;
        CWE476_NULL_Pointer_Dereference__String_81_base baseObject = new CWE476_NULL_Pointer_Dereference__String_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
