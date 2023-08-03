
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Property_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data = null;
        switch (6)
        {
        case 6:
            data = System.getProperty("user.home");
            break;
        default:
            data = null;
            break;
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private void goodG2B1() throws Throwable
    {
        String data = null;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "Testing.test";
            break;
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private void goodG2B2() throws Throwable
    {
        String data = null;
        switch (6)
        {
        case 6:
            data = "Testing.test";
            break;
        default:
            data = null;
            break;
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
