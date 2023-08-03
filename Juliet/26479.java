
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_61b
{
    public int badSource() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        return data;
    }
    public int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
}
