
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__binary_if_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
        {
            String myString = null;
            if ((myString != null) & (myString.length() > 0))
            {
                IO.writeLine("The string length is greater than 0");
            }
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
        {
            String myString = null;
            if ((myString != null) && (myString.length() > 0))
            {
                IO.writeLine("The string length is greater than 0");
            }
        }
        break;
        }
    }
    private void good2() throws Throwable
    {
        switch (7)
        {
        case 7:
        {
            String myString = null;
            if ((myString != null) && (myString.length() > 0))
            {
                IO.writeLine("The string length is greater than 0");
            }
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
