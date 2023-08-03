
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Environment_format_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            System.out.format(data);
        }
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
}
