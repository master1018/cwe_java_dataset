
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_console_readLine_multiply_22b
{
    public void badSink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_console_readLine_multiply_22a.badPublicStatic)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_console_readLine_multiply_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Short.MIN_VALUE/2))
                {
                    short result = (short)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    public void goodB2G2Sink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_console_readLine_multiply_22a.goodB2G2PublicStatic)
        {
            if(data < 0) 
            {
                if (data > (Short.MIN_VALUE/2))
                {
                    short result = (short)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__short_console_readLine_multiply_22a.goodG2BPublicStatic)
        {
            if(data < 0) 
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0;
        }
    }
}
