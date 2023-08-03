
package testcases.CWE586_Explicit_Call_to_Finalize;
import testcasesupport.*;
public class CWE586_Explicit_Call_to_Finalize__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        CWE586_Explicit_Call_to_Finalize__basic_Helper badObj = new CWE586_Explicit_Call_to_Finalize__basic_Helper();
        try
        {
            badObj.sayHello();
        }
        catch (Exception exception)
        {
            IO.writeLine("An error occurred.");
        }
        finally
        {
            badObj.finalize();
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        CWE586_Explicit_Call_to_Finalize__basic_Helper goodObj = new CWE586_Explicit_Call_to_Finalize__basic_Helper();
        try
        {
            goodObj.sayHello();
        }
        catch (Exception exception)
        {
            IO.writeLine("An error occurred.");
        }
        finally
        {
            goodObj = null;
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
