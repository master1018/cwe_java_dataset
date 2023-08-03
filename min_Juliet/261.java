
package testcases.CWE395_Catch_NullPointerException;
import testcasesupport.*;
public class CWE395_Catch_NullPointerException__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String systemProperty = System.getProperty("CWE395");
        try
        {
            if(systemProperty.equals("CWE395"))
            {
                IO.writeLine("systemProperty is CWE395");
            }
        }
        catch (NullPointerException exceptNullPointer) 
        {
            IO.writeLine("systemProperty is null");
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        String systemProperty = System.getProperty("CWE395");
        if (systemProperty != null) 
        {
            if (systemProperty.equals("CWE395"))
            {
                IO.writeLine("systemProperty is CWE395");
            }
        }
        else
        {
            IO.writeLine("systemProperty is null");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
