
package testcases.CWE499_Sensitive_Data_Serializable;
import java.io.*;
import testcasesupport.*;
public class CWE499_Sensitive_Data_Serializable__serializable_Helper extends AbstractTestCaseClassIssue implements Serializable
{
	private static final long serialVersionUID = 100000L;
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
