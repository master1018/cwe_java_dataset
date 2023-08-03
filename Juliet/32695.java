
package testcases.CWE193_Off_by_One_Error;
import testcasesupport.*;
public class CWE193_Off_by_One_Error__do_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            int[] intArray = new int[10];
            int i = 0;
            do
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
            while (i <= intArray.length);   
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int[] intArray = new int[10];
            int i = 0;
            do
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
            while (i < intArray.length);   
        }
    }
    private void good2() throws Throwable
    {
        if (IO.staticReturnsTrue())
        {
            int[] intArray = new int[10];
            int i = 0;
            do
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
            while (i < intArray.length);   
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
