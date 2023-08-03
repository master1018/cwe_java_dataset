
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__max_value_write_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        Vector<Integer> countVector = new Vector<Integer>(5);
        countVector.add(0, count);
        countVector.add(1, count);
        countVector.add(2, count);
        (new CWE400_Resource_Exhaustion__max_value_write_72b()).badSink(countVector  );
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
        Vector<Integer> countVector = new Vector<Integer>(5);
        countVector.add(0, count);
        countVector.add(1, count);
        countVector.add(2, count);
        (new CWE400_Resource_Exhaustion__max_value_write_72b()).goodG2BSink(countVector  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        Vector<Integer> countVector = new Vector<Integer>(5);
        countVector.add(0, count);
        countVector.add(1, count);
        countVector.add(2, count);
        (new CWE400_Resource_Exhaustion__max_value_write_72b()).goodB2GSink(countVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
