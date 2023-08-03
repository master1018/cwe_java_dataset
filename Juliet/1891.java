
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Property_67b
{
    public void badSink(CWE470_Unsafe_Reflection__Property_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void goodG2BSink(CWE470_Unsafe_Reflection__Property_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
}
