
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_PropertiesFile_multiply_81_goodG2B extends CWE190_Integer_Overflow__int_PropertiesFile_multiply_81_base
{
    public void action(int data ) throws Throwable
    {
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
}
