
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__Environment_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE643_Xpath_Injection__Environment_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE643_Xpath_Injection__Environment_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE643_Xpath_Injection__Environment_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
