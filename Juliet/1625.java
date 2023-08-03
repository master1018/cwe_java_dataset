
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
import java.util.HashMap;
public class CWE134_Uncontrolled_Format_String__database_printf_74b
{
    public void badSink(HashMap<Integer,String> dataHashMap ) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodG2BSink(HashMap<Integer,String> dataHashMap ) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            System.out.printf(data);
        }
    }
    public void goodB2GSink(HashMap<Integer,String> dataHashMap ) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            System.out.printf("%s%n", data);
        }
    }
}
