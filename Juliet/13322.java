
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Property_divide_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE369_Divide_by_Zero__int_Property_divide_61b()).badSource();
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE369_Divide_by_Zero__int_Property_divide_61b()).goodG2BSource();
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    private void goodB2G() throws Throwable
    {
        int data = (new CWE369_Divide_by_Zero__int_Property_divide_61b()).goodB2GSource();
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
