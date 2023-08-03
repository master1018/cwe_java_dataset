
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_getQueryString_Servlet_divide_66b
{
    public void badSink(int dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataArray[2];
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodG2BSink(int dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataArray[2];
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodB2GSink(int dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataArray[2];
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
