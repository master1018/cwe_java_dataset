
package testcases.CWE568_Finalize_Without_Super;
import java.util.logging.Level;
import testcasesupport.AbstractTestCaseClassIssueGood;
import testcasesupport.IO;
public class CWE568_Finalize_Without_Super__empty_01_good1 extends AbstractTestCaseClassIssueGood 
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
    public class GoodClass extends HelperClass 
    {        
        protected void finalize() 
        {
            try 
            {
                IO.writeLine("finalizing GoodClass");
            }
            finally
            {
                try 
                {
                    super.finalize();
                } 
                catch (Throwable exceptThrowable) 
                {
                    IO.logger.log(Level.WARNING, "caught an exception calling super.finalize() from the GoodClass", exceptThrowable);
                }
            }
        }
    }
    public void good() throws Throwable 
    {
        good1();
    }
    private void good1()
    {
        GoodClass objectGood = new GoodClass();
        try
        {
            objectGood.printMessage();
        } 
        finally 
        {
            objectGood = null; 
        }
    }
    public static void main(String[] args) 
        throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
