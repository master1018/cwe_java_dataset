
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__equals_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int intOne = 1;
        IO.writeLine(intOne);
        intOne = intOne;
        IO.writeLine(intOne);
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        int intOne = 1, intFive = 5;
        IO.writeLine(intOne);
        intOne = intFive;
        IO.writeLine(intOne);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
