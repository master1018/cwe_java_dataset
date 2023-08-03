
package testcases.CWE835_Infinite_Loop;
import testcasesupport.*;
public class CWE835_Infinite_Loop__while_true_01 extends AbstractTestCase 
{
    public void bad()
    {
        int i = 0;
        while(true)
        {
            IO.writeLine(i);
            i++;
        }
    }
    private void good1() 
    {
        int i = 0;
        while(true)
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
