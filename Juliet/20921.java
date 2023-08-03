
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_add_81_bad extends CWE190_Integer_Overflow__int_max_add_81_base
{
    public void action(int data ) throws Throwable
    {
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
}
