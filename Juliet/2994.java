
package testcases.CWE571_Expression_Always_True;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
public class CWE571_Expression_Always_True__private_static_final_01 extends AbstractTestCase 
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    public void bad()
    {
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            IO.writeLine("always prints");
        }
    }
    public void good()
    {
        good1();
    }
    private void good1()
    {
        if (IO.staticReturnsTrueOrFalse() == PRIVATE_STATIC_FINAL_TRUE)
        {
            IO.writeLine("sometimes prints");
        }
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
