
package testcases.CWE396_Catch_Generic_Exception;
import testcasesupport.*;
public class CWE396_Catch_Generic_Exception__Exception_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
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
        }
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
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
