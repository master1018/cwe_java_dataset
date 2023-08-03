
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
