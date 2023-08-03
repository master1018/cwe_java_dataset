
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_max_add_81_goodG2B extends CWE190_Integer_Overflow__long_max_add_81_base
{
    public void action(long data ) throws Throwable
    {
        long result = (long)(data + 1);
        IO.writeLine("result: " + result);
    }
}
