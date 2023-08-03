
package testcases.CWE209_Information_Leak_Error;
import testcasesupport.*;
public class CWE209_Information_Leak_Error__printStackTrace_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (true)
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
    }
    private void good1() throws Throwable
    {
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
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
    }
    private void good2() throws Throwable
    {
        if (true)
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
