
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_Environment_divide_41 extends AbstractTestCase
{
    private void badSink(int data ) throws Throwable
    {
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void bad() throws Throwable
    {
        int data;
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
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(int data ) throws Throwable
    {
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        goodG2BSink(data  );
    }
    private void goodB2GSink(int data ) throws Throwable
    {
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
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
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
