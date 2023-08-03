
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_sub_81_bad extends CWE191_Integer_Underflow__byte_rand_sub_81_base
{
    public void action(byte data ) throws Throwable
    {
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
}
