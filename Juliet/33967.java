
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_multiply_81_bad extends CWE190_Integer_Overflow__short_rand_multiply_81_base
{
    public void action(short data ) throws Throwable
    {
        if(data > 0) 
        {
            short result = (short)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
