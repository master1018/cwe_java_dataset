
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_22b
{
    public int badSource() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_22a.badPublicStatic)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public int goodG2B1Source() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_22a.goodG2B1PublicStatic)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    public int goodG2B2Source() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_22a.goodG2B2PublicStatic)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
}
