
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_Property_22b
{
    public void badSink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__sleep_Property_22a.badPublicStatic)
        {
            Thread.sleep(count);
        }
        else
        {
            count = 0;
        }
    }
    public void goodB2G1Sink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__sleep_Property_22a.goodB2G1PublicStatic)
        {
            count = 0;
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    public void goodB2G2Sink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__sleep_Property_22a.goodB2G2PublicStatic)
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
        else
        {
            count = 0;
        }
    }
    public void goodG2BSink(int count ) throws Throwable
    {
        if (CWE400_Resource_Exhaustion__sleep_Property_22a.goodG2BPublicStatic)
        {
            Thread.sleep(count);
        }
        else
        {
            count = 0;
        }
    }
}
