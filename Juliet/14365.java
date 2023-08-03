
package testcases.CWE568_Finalize_Without_Super;
import java.util.logging.Level;
import testcasesupport.AbstractTestCaseClassIssueBad;
import testcasesupport.IO;
public class CWE568_Finalize_Without_Super__from_console_01_bad extends AbstractTestCaseClassIssueBad 
{
    public class HelperClass 
    {
        private String message;
        public HelperClass() 
        {
            this.message = "hello world";
        }
        public void printMessage() 
        {
            IO.writeLine(this.message);
        }
        protected void finalize() 
        {
            try
            {
                IO.writeLine("finalizing HelperClass");
            }
            finally
            {
                try
                {
                    super.finalize();
                }
                catch (Throwable exceptThrowable)
                {
                    IO.logger.log(Level.WARNING, "caught an exception calling super.finalize() from the HelperClass", exceptThrowable);
                }
            }
        }
    }
    public class BadClass extends HelperClass 
    { 
        protected void finalize() 
        {
            IO.writeLine("finalizing BadClass");
        }
    }
    public void bad() throws Throwable 
    {
        BadClass objectBad = new BadClass();
        try
        {
            objectBad.printMessage();
        }
        finally 
        {
            objectBad = null;
        }
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
