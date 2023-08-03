
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_Environment_modulo_22b
{
    public void badSink(float data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__float_Environment_modulo_22a.badPublicStatic)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
        else
        {
            data = 0.0f;
        }
    }
    public void goodB2G1Sink(float data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__float_Environment_modulo_22a.goodB2G1PublicStatic)
        {
            data = 0.0f;
        }
        else
        {
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 % data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
    }
    public void goodB2G2Sink(float data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__float_Environment_modulo_22a.goodB2G2PublicStatic)
        {
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 % data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
        else
        {
            data = 0.0f;
        }
    }
    public void goodG2BSink(float data ) throws Throwable
    {
        if (CWE369_Divide_by_Zero__float_Environment_modulo_22a.goodG2BPublicStatic)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
        else
        {
            data = 0.0f;
        }
    }
}
