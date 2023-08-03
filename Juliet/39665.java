
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_divide_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        return data;
    }
    public void bad() throws Throwable
    {
        int data = badSource();
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    private int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        int data = goodG2BSource();
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    private int goodB2GSource() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        return data;
    }
    private void goodB2G() throws Throwable
    {
        int data = goodB2GSource();
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
