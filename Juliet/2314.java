
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_File_to_short_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int data;
        badPublicStatic = true;
        data = (new CWE197_Numeric_Truncation_Error__int_File_to_short_22b()).badSource();
        {
            IO.writeLine((short)data);
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
        int data;
        goodG2B1PublicStatic = false;
        data = (new CWE197_Numeric_Truncation_Error__int_File_to_short_22b()).goodG2B1Source();
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2PublicStatic = true;
        data = (new CWE197_Numeric_Truncation_Error__int_File_to_short_22b()).goodG2B2Source();
        {
            IO.writeLine((short)data);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
