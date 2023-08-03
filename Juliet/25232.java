
package testcases.CWE209_Information_Leak_Error;
import testcasesupport.*;
public class CWE209_Information_Leak_Error__printStackTrace_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        try
        {
            throw new UnsupportedOperationException();
        }
        catch (UnsupportedOperationException exceptUnsupportedOperation)
        {
            exceptUnsupportedOperation.printStackTrace(); 
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
            throw new UnsupportedOperationException();
        }
        catch (UnsupportedOperationException exceptUnsupportedOperation)
        {
            IO.writeLine("There was an unsupported operation error"); 
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
