
package testcases.CWE835_Infinite_Loop;
import testcasesupport.*;
public class CWE835_Infinite_Loop__for_empty_01 extends AbstractTestCase 
{    
    public void bad()
    {
        int i = 0;
        for (;;)
        {
            IO.writeLine(i);
            i++;
        }
    }
    private void good1() 
    {
        int i = 0;
        for (;;)
        {
            if (i == 10) 
            { 
                break; 
            }
            IO.writeLine(i);
            i++;
        }
    }
    public void good()  
    {
        good1();
    }    
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
