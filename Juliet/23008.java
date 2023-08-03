
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__max_value_for_loop_68a extends AbstractTestCase
{
    public static int count;
    public void bad() throws Throwable
    {
        count = Integer.MAX_VALUE;
        (new CWE400_Resource_Exhaustion__max_value_for_loop_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        count = 2;
        (new CWE400_Resource_Exhaustion__max_value_for_loop_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        count = Integer.MAX_VALUE;
        (new CWE400_Resource_Exhaustion__max_value_for_loop_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
