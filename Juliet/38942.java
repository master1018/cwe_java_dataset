
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_HashMap_22b
{
    public int badSource() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__Environment_HashMap_22a.badPublicStatic)
        {
            data = Integer.MIN_VALUE; 
            {
                String stringNumber = System.getenv("ADD");
                if (stringNumber != null) 
                {
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                    }
                }
            }
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
        if (CWE789_Uncontrolled_Mem_Alloc__Environment_HashMap_22a.goodG2B1PublicStatic)
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
        if (CWE789_Uncontrolled_Mem_Alloc__Environment_HashMap_22a.goodG2B2PublicStatic)
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
