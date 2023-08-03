
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Property_modulo_81_bad extends CWE369_Divide_by_Zero__int_Property_modulo_81_base
{
    public void action(int data ) throws Throwable
    {
        IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
    }
}
