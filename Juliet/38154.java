
package testcases.CWE835_Infinite_Loop;
import testcasesupport.*;
public class CWE835_Infinite_Loop__do_01 extends AbstractTestCase 
{
    public void bad()
    {
        int i = 0;
        do 
        {
            IO.writeLine(i);
            i = (i + 1) % 256;
        } while(i >= 0);
    }
    private void good1() 
    {
        int i = 0;
        do 
        {
            if (i == 10) 
            { 
                break; 
            }
            IO.writeLine(i);
            i = (i + 1) % 256;
        } while(i >= 0);
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
