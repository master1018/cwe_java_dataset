
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_divide_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = 0; 
        }
        else
        {
            data = 0;
        }
        if (privateTrue)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (privateFalse)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (privateTrue)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (privateTrue)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = 0; 
        }
        else
        {
            data = 0;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
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
    private void goodB2G2() throws Throwable
    {
        int data;
        if (privateTrue)
        {
            data = 0; 
        }
        else
        {
            data = 0;
        }
        if (privateTrue)
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
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
