
package testcases.CWE193_Off_by_One_Error;
import testcasesupport.*;
public class CWE193_Off_by_One_Error__while_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            int[] intArray = new int[10];
            int i = 0;
            while (i <= intArray.length)
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
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
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            int[] intArray = new int[10];
            int i = 0;
            while (i < intArray.length)
            {
                IO.writeLine("intArray[" + i + "] = " + (intArray[i] = i));
                i++;
            }
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
