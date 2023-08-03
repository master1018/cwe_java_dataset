
package testcases.CWE396_Catch_Generic_Exception;
import testcasesupport.*;
public class CWE396_Catch_Generic_Exception__Throwable_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        try
        {
            Integer.parseInt("Test"); 
        }
        catch (Throwable throwable) 
        {
            IO.writeLine("Caught Throwable");
            throw throwable; 
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        try
        {
            Integer.parseInt("Test"); 
        }
        catch (NumberFormatException exceptNumberFormat) 
        {
            IO.writeLine("Caught Exception");
            throw exceptNumberFormat; 
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
