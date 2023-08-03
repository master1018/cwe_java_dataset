
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_multiply_81_goodG2B extends CWE191_Integer_Underflow__short_rand_multiply_81_base
{
    public void action(short data ) throws Throwable
    {
        if(data < 0) 
        {
            short result = (short)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
