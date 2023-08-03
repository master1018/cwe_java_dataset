
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_listen_tcp_sub_81_goodG2B extends CWE191_Integer_Underflow__int_listen_tcp_sub_81_base
{
    public void action(int data ) throws Throwable
    {
        int result = (int)(data - 1);
        IO.writeLine("result: " + result);
    }
}
