
package testcases.CWE607_Public_Static_Final_Mutable;
import java.util.Calendar;
import java.util.Date;
import testcasesupport.*;
public class CWE607_Public_Static_Final_Mutable__console_01_bad extends AbstractTestCaseClassIssueBad 
{
    public final static Date date = new Date(); 
    public String getHello()
    {
        return "Hello!" + date.getTime();
    }
    public Date getDate()
    {
        return date; 
    }
    public void bad() 
    {
        IO.writeLine("Before: " + this.getHello());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 01, 01);
        this.getDate().setTime(calendar.getTimeInMillis());
        IO.writeLine("After: " + this.getHello());
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
