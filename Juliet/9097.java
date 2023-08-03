
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__heap_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
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
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1() throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
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
            break;
        }
    }
    private void good2() throws Throwable
    {
        switch (7)
        {
        case 7:
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
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
