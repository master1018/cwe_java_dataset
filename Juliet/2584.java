
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_multiply_81_goodB2G extends CWE191_Integer_Underflow__byte_rand_multiply_81_base
{
    public void action(byte data ) throws Throwable
    {
        if(data < 0) 
        {
            if (data > (Byte.MIN_VALUE/2))
            {
                byte result = (byte)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
