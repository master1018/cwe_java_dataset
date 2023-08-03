
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Environment_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        CWE606_Unchecked_Loop_Condition__Environment_81_base baseObject = new CWE606_Unchecked_Loop_Condition__Environment_81_bad();
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
        data = "5";
        CWE606_Unchecked_Loop_Condition__Environment_81_base baseObject = new CWE606_Unchecked_Loop_Condition__Environment_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        CWE606_Unchecked_Loop_Condition__Environment_81_base baseObject = new CWE606_Unchecked_Loop_Condition__Environment_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
