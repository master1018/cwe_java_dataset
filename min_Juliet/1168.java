
package testcases.CWE586_Explicit_Call_to_Finalize;
import java.util.logging.Level;
import testcasesupport.IO;
public class CWE586_Explicit_Call_to_Finalize__basic_Helper 
{
    private String stringHelloWorld;
    public CWE586_Explicit_Call_to_Finalize__basic_Helper() 
    {
        this.stringHelloWorld = new String("hello world");
    }
    public void sayHello() 
    {
        IO.writeLine(this.stringHelloWorld);
    }
    protected void finalize() 
    {
        try
        {
            super.finalize();
        }
        catch(Throwable exceptThrowable)
        {
            IO.logger.log(Level.WARNING, "Error finalizing", exceptThrowable);
        }
        IO.writeLine("finalizing TestObject");
    }
}
