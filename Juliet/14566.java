
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_public_member_variable_01_bad extends AbstractTestCaseClassIssueBad 
{
    public int intBad; 
    public void bad() 
    {    
        IO.writeLine("" + 5);
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
