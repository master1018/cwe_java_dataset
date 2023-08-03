
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Property_divide_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE369_Divide_by_Zero__int_Property_divide_68a.data;
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE369_Divide_by_Zero__int_Property_divide_68a.data;
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodB2GSink() throws Throwable
    {
        int data = CWE369_Divide_by_Zero__int_Property_divide_68a.data;
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
