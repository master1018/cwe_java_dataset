
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        StringBuilder data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
            break;
        }
        switch (7)
        {
        case 7:
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            break;
        default:
            data = null;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        StringBuilder data;
        switch (6)
        {
        case 6:
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            if (data != null)
            {
                String stringTrimmed = data.toString().trim();
                IO.writeLine(stringTrimmed);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
