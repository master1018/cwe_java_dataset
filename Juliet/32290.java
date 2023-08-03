
package testcases.CWE396_Catch_Generic_Exception;
import testcasesupport.*;
public class CWE396_Catch_Generic_Exception__Exception_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            try
            {
                Integer.parseInt("Test"); 
            }
            catch (Exception exception) 
            {
                IO.writeLine("Caught Exception");
                throw exception; 
            }
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
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
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
