
package testcases.CWE510_Trapdoor;
import testcasesupport.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.logging.Level;
public class CWE510_Trapdoor__network_connection_07 extends AbstractTestCaseBadOnly
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        if (privateFive == 5)
        {
            InputStream streamInput = null;
            try
            {
                URL url = new URL("http:
                streamInput = url.openStream();
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "caught IOException", exceptIO);
            }
            finally
            {
                try
                {
                    if (streamInput != null)
                    {
                        streamInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "caught IOException", exceptIO);
                }
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
