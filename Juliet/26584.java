
package testcases.CWE209_Information_Leak_Error;
import testcasesupport.*;
public class CWE209_Information_Leak_Error__printStackTrace_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            try
            {
                throw new UnsupportedOperationException();
            }
            catch (UnsupportedOperationException exceptUnsupportedOperation)
            {
                exceptUnsupportedOperation.printStackTrace(); 
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
                throw new UnsupportedOperationException();
            }
            catch (UnsupportedOperationException exceptUnsupportedOperation)
            {
                IO.writeLine("There was an unsupported operation error"); 
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
