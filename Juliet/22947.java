
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_Environment_divide_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
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
        badPrivate = true;
        badSink(data );
    }
    private void badSink(int data ) throws Throwable
    {
        if (badPrivate)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    private boolean goodB2G1Private = false;
    private boolean goodB2G2Private = false;
    private boolean goodG2BPrivate = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
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
        goodB2G1Private = false;
        goodB2G1Sink(data );
    }
    private void goodB2G1Sink(int data ) throws Throwable
    {
        if (goodB2G1Private)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
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
    }
    private void goodB2G2() throws Throwable
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
        goodB2G2Private = true;
        goodB2G2Sink(data );
    }
    private void goodB2G2Sink(int data ) throws Throwable
    {
        if (goodB2G2Private)
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
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        goodG2BPrivate = true;
        goodG2BSink(data );
    }
    private void goodG2BSink(int data ) throws Throwable
    {
        if (goodG2BPrivate)
        {
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
