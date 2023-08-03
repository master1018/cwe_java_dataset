
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_console_readLine_square_81_bad extends CWE190_Integer_Overflow__long_console_readLine_square_81_base
{
    public void action(long data ) throws Throwable
    {
        long result = (long)(data * data);
        IO.writeLine("result: " + result);
    }
}
