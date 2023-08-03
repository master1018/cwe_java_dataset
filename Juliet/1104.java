
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_console_readLine_multiply_81_bad extends CWE190_Integer_Overflow__long_console_readLine_multiply_81_base
{
    public void action(long data ) throws Throwable
    {
        if(data > 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
