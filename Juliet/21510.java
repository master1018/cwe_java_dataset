
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_rand_sub_22b
{
    public void badSink(byte data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__byte_rand_sub_22a.badPublicStatic)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(byte data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__byte_rand_sub_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if (data > Byte.MIN_VALUE)
            {
                byte result = (byte)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    public void goodB2G2Sink(byte data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__byte_rand_sub_22a.goodB2G2PublicStatic)
        {
            if (data > Byte.MIN_VALUE)
            {
                byte result = (byte)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(byte data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__byte_rand_sub_22a.goodG2BPublicStatic)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
