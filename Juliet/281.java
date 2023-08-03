
package testcases.CWE36_Absolute_Path_Traversal;
import testcasesupport.*;
import java.io.*;
import javax.servlet.http.*;
public class CWE36_Absolute_Path_Traversal__Property_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        CWE36_Absolute_Path_Traversal__Property_81_base baseObject = new CWE36_Absolute_Path_Traversal__Property_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        CWE36_Absolute_Path_Traversal__Property_81_base baseObject = new CWE36_Absolute_Path_Traversal__Property_81_goodG2B();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
