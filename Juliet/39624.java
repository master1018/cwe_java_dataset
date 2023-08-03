
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        int data;
        badPrivate = true;
        data = bad_source();
        HashMap intHashMap = new HashMap(data);
    }
    private int bad_source() throws Throwable
    {
        int data;
        if (badPrivate)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        goodG2B1_private = false;
        data = goodG2B1_source();
        HashMap intHashMap = new HashMap(data);
    }
    private int goodG2B1_source() throws Throwable
    {
        int data = 0;
        if (goodG2B1_private)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2_private = true;
        data = goodG2B2_source();
        HashMap intHashMap = new HashMap(data);
    }
    private int goodG2B2_source() throws Throwable
    {
        int data = 0;
        if (goodG2B2_private)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
