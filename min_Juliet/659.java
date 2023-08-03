
package testcases.CWE478_Missing_Default_Case_in_Switch;
import testcasesupport.*;
import java.io.*;
import java.security.SecureRandom;
public class CWE478_Missing_Default_Case_in_Switch__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String stringIntValue = "";
        int x = (new SecureRandom()).nextInt(3);
        switch (x)
        {
        case 0:
            stringIntValue = "0";
            break;
        case 1:
            stringIntValue = "1";
            break;
        }
        IO.writeLine(stringIntValue);
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        String stringIntValue = "";
        int x = (new SecureRandom()).nextInt(3);
        switch (x)
        {
        case 0:
            stringIntValue = "0";
            break;
        case 1:
            stringIntValue = "1";
            break;
        default:
            stringIntValue = "2";
        }
        IO.writeLine(stringIntValue);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
