
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Environment_42 extends AbstractTestCase
{
    private String badSource() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        return data;
    }
    public void bad() throws Throwable
    {
        String data = badSource();
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private String goodG2BSource() throws Throwable
    {
        String data;
        data = "Testing.test";
        return data;
    }
    private void goodG2B() throws Throwable
    {
        String data = goodG2BSource();
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
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
