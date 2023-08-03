
package testcases.CWE193_Off_by_One_Error;
import testcasesupport.*;
public class CWE193_Off_by_One_Error__while_09 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.STATIC_FINAL_TRUE)
        {
            int[] intArray = new int[10];
            int i = 0;
            while (i <= intArray.length)
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
        }
    }
    private void good1() throws Throwable
    {
        if (IO.STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            int[] intArray = new int[10];
            int i = 0;
            while (i < intArray.length)
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
        }
    }
    private void good2() throws Throwable
    {
        if (IO.STATIC_FINAL_TRUE)
        {
            int[] intArray = new int[10];
            int i = 0;
            while (i < intArray.length)
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
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
