
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_URLConnection_multiply_81_bad extends CWE191_Integer_Underflow__int_URLConnection_multiply_81_base
{
    public void action(int data ) throws Throwable
    {
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
