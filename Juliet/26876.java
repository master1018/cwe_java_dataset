
package testcases.CWE396_Catch_Generic_Exception;
import testcasesupport.*;
public class CWE396_Catch_Generic_Exception__Throwable_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
            try
            {
                Integer.parseInt("Test"); 
            }
            catch (Throwable throwable) 
            {
                IO.writeLine("Caught Throwable");
                throw throwable; 
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1() throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            try
            {
                Integer.parseInt("Test"); 
            }
            catch (NumberFormatException exceptNumberFormat) 
            {
                IO.writeLine("Caught Exception");
                throw exceptNumberFormat; 
            }
            break;
        }
    }
    private void good2() throws Throwable
    {
        switch (7)
        {
        case 7:
            try
            {
                Integer.parseInt("Test"); 
            }
            catch (NumberFormatException exceptNumberFormat) 
            {
                IO.writeLine("Caught Exception");
                throw exceptNumberFormat; 
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
