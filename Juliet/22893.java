
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_multiply_81_bad extends CWE191_Integer_Underflow__long_rand_multiply_81_base
{
    public void action(long data ) throws Throwable
    {
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
