
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__listen_tcp_81_goodG2B extends CWE470_Unsafe_Reflection__listen_tcp_81_base
{
    public void action(String data ) throws Throwable
    {
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
}
