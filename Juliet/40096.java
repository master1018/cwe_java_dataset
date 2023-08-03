
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_square_81_goodG2B extends CWE190_Integer_Overflow__short_rand_square_81_base
{
    public void action(short data ) throws Throwable
    {
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
}
