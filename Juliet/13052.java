
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__random_for_loop_22b
{
    public void badSink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__random_for_loop_22a.badPublicStatic)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
        else
        {
            count = 0;
        }
    }
    public void goodB2G1Sink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__random_for_loop_22a.goodB2G1PublicStatic)
        {
            count = 0;
        }
        else
        {
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
            }
        }
    }
    public void goodB2G2Sink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__random_for_loop_22a.goodB2G2PublicStatic)
        {
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
            }
        }
        else
        {
            count = 0;
        }
    }
    public void goodG2BSink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__random_for_loop_22a.goodG2BPublicStatic)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
        else
        {
            count = 0;
        }
    }
}
