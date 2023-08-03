
package testcases.CWE500_Public_Static_Field_Not_Final;
import testcasesupport.*;
public class CWE500_Public_Static_Field_Not_Final__String_01_bad extends AbstractTestCaseClassIssueBad implements Cloneable 
{
    public static String DEFAULT_ERROR = "The value you entered was not understood.  Please try again.";
    public void bad() 
    {    
        IO.writeLine(DEFAULT_ERROR);
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
