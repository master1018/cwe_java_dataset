
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_connect_tcp_divide_81_bad extends CWE369_Divide_by_Zero__int_connect_tcp_divide_81_base
{
    public void action(int data ) throws Throwable
    {
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
}
