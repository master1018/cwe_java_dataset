
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_sub_81_bad extends CWE191_Integer_Underflow__long_rand_sub_81_base
{
    public void action(long data ) throws Throwable
    {
        long result = (long)(data - 1);
        IO.writeLine("result: " + result);
    }
}
