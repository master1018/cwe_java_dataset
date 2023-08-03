
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__equals_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (true)
        {
            int intOne = 1;
            IO.writeLine(intOne);
            intOne = intOne;
            IO.writeLine(intOne);
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
            int intOne = 1, intFive = 5;
            IO.writeLine(intOne);
            intOne = intFive;
            IO.writeLine(intOne);
        }
    }
    private void good2() throws Throwable
    {
        if (true)
        {
            int intOne = 1, intFive = 5;
            IO.writeLine(intOne);
            intOne = intFive;
            IO.writeLine(intOne);
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
