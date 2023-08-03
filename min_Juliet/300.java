
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_block_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            {
            }
            IO.writeLine("Hello from bad()");
        }
        else
        {
            {
                String sentence = "Inside the block"; 
                IO.writeLine(sentence);
            }
            IO.writeLine("Hello from good()");
        }
    }
    private void good1() throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            {
                String sentence = "Inside the block"; 
                IO.writeLine(sentence);
            }
            IO.writeLine("Hello from good()");
        }
        else
        {
            {
                String sentence = "Inside the block"; 
                IO.writeLine(sentence);
            }
            IO.writeLine("Hello from good()");
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
