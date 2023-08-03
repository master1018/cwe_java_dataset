
package testcases.CWE835_Infinite_Loop;
import testcasesupport.*;
public class CWE835_Infinite_Loop__for_01 extends AbstractTestCase 
{
    public void bad()
    {
        for (int i = 0; i >= 0; i = (i + 1) % 256)
        {
            IO.writeLine(i);
        }
    }
    private void good1() 
    {
        for (int i = 0; i >= 0; i = (i + 1) % 256)
        {
            if (i == 10) 
            { 
                break; 
            }
            IO.writeLine(i);
        }
    }
    private void good2() 
    {
        for (int i = 0; i < 11; i = (i + 1) % 256)
        {
            IO.writeLine(i);
        }
    }
    public void good()  
    {
        good1();
        good2();
    }    
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
