
package testcases.CWE484_Omitted_Break_Statement_in_Switch;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE484_Omitted_Break_Statement_in_Switch__basic_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int intRandom = (new SecureRandom()).nextInt(3);
        String stringValue;
        switch (intRandom)
        {
        case 1:
            stringValue = "one";
            break;
        case 2:
            stringValue = "two"; 
        default:
            stringValue = "Default";
            break;
        }
        IO.writeLine(stringValue);
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        int intRandom = (new SecureRandom()).nextInt(3);
        String stringValue;
        switch (intRandom)
        {
        case 1:
            stringValue = "one";
            break;
        case 2:
            stringValue = "two";
            break; 
        default:
            stringValue = "Default";
            break;
        }
        IO.writeLine(stringValue);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
