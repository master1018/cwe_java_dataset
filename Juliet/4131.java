
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_equals_74b
{
    public void badSink(HashMap<Integer,String> dataHashMap ) throws Throwable
    {
        String data = dataHashMap.get(2);
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink(HashMap<Integer,String> dataHashMap ) throws Throwable
    {
        String data = dataHashMap.get(2);
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink(HashMap<Integer,String> dataHashMap ) throws Throwable
    {
        String data = dataHashMap.get(2);
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
