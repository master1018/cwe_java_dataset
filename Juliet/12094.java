
package testcases.CWE89_SQL_Injection.s04;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__Property_executeUpdate_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE89_SQL_Injection__Property_executeUpdate_72b()).badSink(dataVector  );
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
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE89_SQL_Injection__Property_executeUpdate_72b()).goodG2BSink(dataVector  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE89_SQL_Injection__Property_executeUpdate_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
