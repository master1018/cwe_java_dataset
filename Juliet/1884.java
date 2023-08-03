
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Property_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        for (int i = 0; i < 1; i++)
        {
            Class<?> tempClass = Class.forName(data);
            Object tempClassObject = tempClass.newInstance();
            IO.writeLine(tempClassObject.toString()); 
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "Testing.test";
        for (int i = 0; i < 1; i++)
        {
            Class<?> tempClass = Class.forName(data);
            Object tempClassObject = tempClass.newInstance();
            IO.writeLine(tempClassObject.toString()); 
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
