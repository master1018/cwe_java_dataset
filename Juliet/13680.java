
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_console_readLine_square_81_bad extends CWE190_Integer_Overflow__short_console_readLine_square_81_base
{
    public void action(short data ) throws Throwable
    {
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
}
