
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_multiply_81_goodG2B extends CWE191_Integer_Underflow__byte_rand_multiply_81_base
{
    public void action(byte data ) throws Throwable
    {
        if(data < 0) 
        {
            byte result = (byte)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
