
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_console_readLine_divide_22b
{
    public void badSink(int data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_console_readLine_divide_22a.badPublicStatic)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_console_readLine_divide_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
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
    public void goodB2G2Sink(int data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_console_readLine_divide_22a.goodB2G2PublicStatic)
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
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__int_console_readLine_divide_22a.goodG2BPublicStatic)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            data = 0;
        }
    }
}
