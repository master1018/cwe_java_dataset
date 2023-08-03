
package testcases.CWE193_Off_by_One_Error;
import testcasesupport.*;
public class CWE193_Off_by_One_Error__do_01 extends AbstractTestCase
{
    public void bad() throws Throwable
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
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
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
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
