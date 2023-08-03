
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_getParameter_Servlet_divide_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodB2GSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
}
