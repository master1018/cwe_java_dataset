
package testcases.CWE581_Object_Model_Violation;
import testcasesupport.*;
public class CWE581_Object_Model_Violation__hashCode_01_bad extends AbstractTestCaseClassIssueBad 
{
    private String message = "test";
    public void setMessage(String message) 
    {
        this.message = message;
    }
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (object.getClass() != this.getClass())
        {
            return false;
        }
        CWE581_Object_Model_Violation__hashCode_01_bad objectGood1 = (CWE581_Object_Model_Violation__hashCode_01_bad)object;
        if (objectGood1.message.equals(this.message))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void bad() 
    { 
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
