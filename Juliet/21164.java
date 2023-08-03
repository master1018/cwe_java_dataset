
 package testcases.CWE580_Clone_Without_Super;
import java.util.Date;
import testcasesupport.*;
public class CWE580_Clone_Without_Super__clone_01_bad extends AbstractTestCaseClassIssueBad implements Cloneable
{
    private Date theDate = new Date();
    public void setDate(Date theDate) 
    {
        this.theDate = (Date) theDate.clone();
    }
    protected final Object clone() throws CloneNotSupportedException 
    {
        CWE580_Clone_Without_Super__clone_01_bad objectBad = new CWE580_Clone_Without_Super__clone_01_bad();     
        objectBad.setDate(new Date(theDate.getTime()));
        return objectBad;
    }
    public void bad() throws CloneNotSupportedException
    { 
        CWE580_Clone_Without_Super__clone_01_bad myClone = (CWE580_Clone_Without_Super__clone_01_bad)clone();
        myClone.setDate(new Date());
    }
}
