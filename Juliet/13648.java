
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__heap_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            ArrayList<byte[]> byteArrayList = new ArrayList<byte[]>();
            if(true) 
            {
                while(true)
                {
                    byte[] byteArray = new byte[10485760];
                    byteArrayList.add(byteArray);
                    IO.writeLine("" + Runtime.getRuntime().freeMemory());
                }
            }
        }
        else
        {
            ArrayList<byte[]> byteArrayList = new ArrayList<byte[]>();
            if(true) 
            {
                while(true)
                {
                    if (Runtime.getRuntime().freeMemory() < 10485760)
                    {
                        IO.writeLine("Not enough memory to go again");
                        break;
                    }
                    byte[] byteArray = new byte[10485760];
                    byteArrayList.add(byteArray);
                    IO.writeLine("" + Runtime.getRuntime().freeMemory());
                }
            }
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            ArrayList<byte[]> byteArrayList = new ArrayList<byte[]>();
            if(true) 
            {
                while(true)
                {
                    if (Runtime.getRuntime().freeMemory() < 10485760)
                    {
                        IO.writeLine("Not enough memory to go again");
                        break;
                    }
                    byte[] byteArray = new byte[10485760];
                    byteArrayList.add(byteArray);
                    IO.writeLine("" + Runtime.getRuntime().freeMemory());
                }
            }
        }
        else
        {
            ArrayList<byte[]> byteArrayList = new ArrayList<byte[]>();
            if(true) 
            {
                while(true)
                {
                    if (Runtime.getRuntime().freeMemory() < 10485760)
                    {
                        IO.writeLine("Not enough memory to go again");
                        break;
                    }
                    byte[] byteArray = new byte[10485760];
                    byteArrayList.add(byteArray);
                    IO.writeLine("" + Runtime.getRuntime().freeMemory());
                }
            }
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
