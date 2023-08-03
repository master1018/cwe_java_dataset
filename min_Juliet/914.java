
package testcases.CWE586_Explicit_Call_to_Finalize;
import testcasesupport.*;
public class CWE586_Explicit_Call_to_Finalize__basic_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        for(int j = 0; j < 1; j++)
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
    }
    private void good1() throws Throwable
    {
        for(int k = 0; k < 1; k++)
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
