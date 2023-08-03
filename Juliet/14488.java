
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_multiply_81_goodB2G extends CWE191_Integer_Underflow__short_rand_multiply_81_base
{
    public void action(short data ) throws Throwable
    {
        if(data < 0) 
        {
            if (data > (Short.MIN_VALUE/2))
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
