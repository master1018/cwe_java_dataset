
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__getCookies_Servlet_for_loop_72b
{
    public void badSink(Vector<Integer> countVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countVector.remove(2);
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodG2BSink(Vector<Integer> countVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countVector.remove(2);
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodB2GSink(Vector<Integer> countVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countVector.remove(2);
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
}
