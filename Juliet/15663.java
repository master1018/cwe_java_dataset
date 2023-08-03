
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_connect_tcp_sub_81_bad extends CWE191_Integer_Underflow__int_connect_tcp_sub_81_base
{
    public void action(int data ) throws Throwable
    {
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
}
