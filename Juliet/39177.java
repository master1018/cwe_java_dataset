
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_modulo_06 extends AbstractTestCase
{
    private static final int PRIVATE_STATIC_FINAL_FIVE = 5;
    public void bad() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            data = 0; 
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE!=5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            data = 0; 
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_FIVE!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
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
    private void goodB2G2() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE==5)
        {
            data = 0; 
        }
        else
        {
            data = 0;
        }
        if (PRIVATE_STATIC_FINAL_FIVE==5)
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
