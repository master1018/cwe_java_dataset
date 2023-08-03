
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__float_Property_divide_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        float data = 0.0f;
        data = -1.0f; 
        {
            String stringNumber = System.getProperty("user.home");
            if (stringNumber != null)
            {
                try
                {
                    data = Float.parseFloat(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        badPublicStatic = true;
        (new CWE369_Divide_by_Zero__float_Property_divide_22b()).badSink(data );
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        float data = 0.0f;
        data = -1.0f; 
        {
            String stringNumber = System.getProperty("user.home");
            if (stringNumber != null)
            {
                try
                {
                    data = Float.parseFloat(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        goodB2G1PublicStatic = false;
        (new CWE369_Divide_by_Zero__float_Property_divide_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        float data = 0.0f;
        data = -1.0f; 
        {
            String stringNumber = System.getProperty("user.home");
            if (stringNumber != null)
            {
                try
                {
                    data = Float.parseFloat(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        goodB2G2PublicStatic = true;
        (new CWE369_Divide_by_Zero__float_Property_divide_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        float data = 0.0f;
        data = 2.0f;
        goodG2BPublicStatic = true;
        (new CWE369_Divide_by_Zero__float_Property_divide_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
