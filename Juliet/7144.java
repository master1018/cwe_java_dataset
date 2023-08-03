
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
import java.util.Vector;
public class CWE134_Uncontrolled_Format_String__database_printf_72b
{
    public void badSink(Vector<String> dataVector ) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodG2BSink(Vector<String> dataVector ) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodB2GSink(Vector<String> dataVector ) throws Throwable
    {
        String data = dataVector.remove(2);
        if (data != null)
        {
            System.out.printf("%s%n", data);
        }
    }
}
