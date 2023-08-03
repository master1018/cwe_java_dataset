
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_67a extends AbstractTestCase
{
    static class Container
    {
        public int containerOne;
    }
    public void bad() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        Container countContainer = new Container();
        countContainer.containerOne = count;
        (new CWE400_Resource_Exhaustion__sleep_random_67b()).badSink(countContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        Container countContainer = new Container();
        countContainer.containerOne = count;
        (new CWE400_Resource_Exhaustion__sleep_random_67b()).goodG2BSink(countContainer  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        Container countContainer = new Container();
        countContainer.containerOne = count;
        (new CWE400_Resource_Exhaustion__sleep_random_67b()).goodB2GSink(countContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
