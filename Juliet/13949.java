
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_Property_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        short data;
        badPublicStatic = true;
        data = (new CWE197_Numeric_Truncation_Error__short_Property_22b()).badSource();
        {
            IO.writeLine((byte)data);
        }
    }
    public static boolean goodG2B1PublicStatic = false;
    public static boolean goodG2B2PublicStatic = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        goodG2B1PublicStatic = false;
        data = (new CWE197_Numeric_Truncation_Error__short_Property_22b()).goodG2B1Source();
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        goodG2B2PublicStatic = true;
        data = (new CWE197_Numeric_Truncation_Error__short_Property_22b()).goodG2B2Source();
        {
            IO.writeLine((byte)data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
