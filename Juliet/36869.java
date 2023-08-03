
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__float_Environment_divide_61b
{
    public float badSource() throws Throwable
    {
        float data;
        data = -1.0f; 
        {
            String stringNumber = System.getenv("ADD");
            if (stringNumber != null)
            {
                try
                {
                    data = Float.parseFloat(stringNumber.trim());
                }
                catch (NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
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
        data = -1.0f; 
        {
            String stringNumber = System.getenv("ADD");
            if (stringNumber != null)
            {
                try
                {
                    data = Float.parseFloat(stringNumber.trim());
                }
                catch (NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        return data;
    }
}
