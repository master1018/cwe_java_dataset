
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_private_member_value_01_bad extends AbstractTestCaseClassIssueBad 
{
    private int intBad = 5; 
    public void bad() 
    {    
        intBad = 10;
        IO.writeLine("" + intBad);
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
