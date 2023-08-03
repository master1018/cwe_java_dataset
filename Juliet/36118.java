
package testcases.CWE580_Clone_Without_Super;
import java.util.Date;
import testcasesupport.*;
public class CWE580_Clone_Without_Super__clone_01_good1 extends AbstractTestCaseClassIssueGood implements Cloneable
{
    private Date theDate = new Date();
    public void setDate(Date theDate) 
    {
        this.theDate = (Date) theDate.clone();
    }
    protected final Object clone() throws CloneNotSupportedException 
    {
        CWE580_Clone_Without_Super__clone_01_good1 objectGood1 = (CWE580_Clone_Without_Super__clone_01_good1) super.clone();          
        objectGood1.setDate(new Date(theDate.getTime()));
        return objectGood1;
    }
    public void good1() throws CloneNotSupportedException
    { 
        CWE580_Clone_Without_Super__clone_01_good1 myClone = (CWE580_Clone_Without_Super__clone_01_good1)clone();
        myClone.setDate(new Date());
    }
    public void good() throws CloneNotSupportedException
    {
        good1();
    }
}
