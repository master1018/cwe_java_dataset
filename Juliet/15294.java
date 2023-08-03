
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_divide_61b
{
    public float badSource() throws Throwable
    {
        float data;
        data = 0.0f; 
        return data;
    }
    public float goodG2BSource() throws Throwable
    {
        float data;
        data = 2.0f;
        return data;
    }
    public float goodB2GSource() throws Throwable
    {
        float data;
        data = 0.0f; 
        return data;
    }
}
