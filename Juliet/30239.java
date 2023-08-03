
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_PropertiesFile_multiply_22b
{
    public void badSink(int data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_PropertiesFile_multiply_22a.badPublicStatic)
        {
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_PropertiesFile_multiply_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Integer.MIN_VALUE/2))
                {
                    int result = (int)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    public void goodB2G2Sink(int data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_PropertiesFile_multiply_22a.goodB2G2PublicStatic)
        {
            if(data < 0) 
            {
                if (data > (Integer.MIN_VALUE/2))
                {
                    int result = (int)(data * 2);
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
    public void goodG2BSink(int data ) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_PropertiesFile_multiply_22a.goodG2BPublicStatic)
        {
            if(data < 0) 
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
        else
        {
            data = 0;
        }
    }
}
