
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Environment_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE606_Unchecked_Loop_Condition__Environment_51b()).badSink(data  );
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
        (new CWE606_Unchecked_Loop_Condition__Environment_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE606_Unchecked_Loop_Condition__Environment_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
