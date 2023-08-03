
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_StringBuilder_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        badPrivate = true;
        badSink(data );
    }
    private void badSink(StringBuilder data ) throws Throwable
    {
        if (badPrivate)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private boolean goodB2G1Private = false;
    private boolean goodB2G2Private = false;
    private boolean goodG2BPrivate = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        goodB2G1Private = false;
        goodB2G1Sink(data );
    }
    private void goodB2G1Sink(StringBuilder data ) throws Throwable
    {
        if (goodB2G1Private)
        {
            IO.writeLine("Benign, fixed string");
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
    private void goodB2G2() throws Throwable
    {
        StringBuilder data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderBad();
        goodB2G2Private = true;
        goodB2G2Sink(data );
    }
    private void goodB2G2Sink(StringBuilder data ) throws Throwable
    {
        if (goodB2G2Private)
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
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBuilderGood();
        goodG2BPrivate = true;
        goodG2BSink(data );
    }
    private void goodG2BSink(StringBuilder data ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            String stringTrimmed = data.toString().trim();
            IO.writeLine(stringTrimmed);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
