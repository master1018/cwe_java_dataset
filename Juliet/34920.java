
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        StringBuilder data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
        else
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        StringBuilder data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        }
        if(IO.staticReturnsTrueOrFalse())
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
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
