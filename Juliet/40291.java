
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_divide_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (5==5)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (5!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (5==5)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (5==5)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (5==5)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (5!=5)
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
        if (5==5)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (5==5)
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
