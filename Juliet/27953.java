
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_connect_tcp_modulo_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data = (new CWE369_Divide_by_Zero__float_connect_tcp_modulo_61b()).badSource();
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float data = (new CWE369_Divide_by_Zero__float_connect_tcp_modulo_61b()).goodG2BSource();
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    private void goodB2G() throws Throwable
    {
        float data = (new CWE369_Divide_by_Zero__float_connect_tcp_modulo_61b()).goodB2GSource();
        if (Math.abs(data) > 0.000001)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
        else
        {
            IO.writeLine("This would result in a modulo by zero");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
