
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_modulo_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 0; 
        for (int j = 0; j < 1; j++)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        for (int j = 0; j < 1; j++)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 0; 
        for (int k = 0; k < 1; k++)
        {
            if (data != 0)
            {
                IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
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
