
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Environment_67a extends AbstractTestCase
{
    static class Container
    {
        public String containerOne;
    }
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE606_Unchecked_Loop_Condition__Environment_67b()).badSink(dataContainer  );
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
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE606_Unchecked_Loop_Condition__Environment_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE606_Unchecked_Loop_Condition__Environment_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
