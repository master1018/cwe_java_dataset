
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_81_bad extends CWE190_Integer_Overflow__short_max_add_81_base
{
    public void action(short data ) throws Throwable
    {
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
}
