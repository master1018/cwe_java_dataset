
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_database_add_81_bad extends CWE190_Integer_Overflow__int_database_add_81_base
{
    public void action(int data ) throws Throwable
    {
        int result = (int)(data + 1);
        IO.writeLine("result: " + result);
    }
}