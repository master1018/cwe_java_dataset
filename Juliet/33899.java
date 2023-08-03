
package testcases.CWE477_Obsolete_Functions;
import testcasesupport.*;
public class CWE477_Obsolete_Functions__Date_parse_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        while(true)
        {
            long unixDate = java.util.Date.parse("2010-07-13 10:41:00");
            IO.writeLine(unixDate); 
            break;
        }
    }
    private void good1() throws Throwable
    {
        while(true)
        {
            java.util.Date date = java.text.DateFormat.getInstance().parse("2010-07-13 10:41:00");
            IO.writeLine(date.toString()); 
            break;
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
