
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Environment_modulo_81_goodG2B extends CWE369_Divide_by_Zero__int_Environment_modulo_81_base
{
    public void action(int data ) throws Throwable
    {
        IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
    }
}
