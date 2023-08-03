
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_listen_tcp_divide_51b
{
    public void badSink(int data ) throws Throwable
    {
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodB2GSink(int data ) throws Throwable
    {
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
