
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_ArrayList_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        while (true)
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
            break;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B() throws Throwable
    {
        int data;
        while (true)
        {
            data = 2;
            break;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
