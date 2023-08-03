
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_console_readLine_add_81_bad extends CWE190_Integer_Overflow__byte_console_readLine_add_81_base
{
    public void action(byte data ) throws Throwable
    {
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
}
