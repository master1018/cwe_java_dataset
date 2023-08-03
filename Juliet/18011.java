
package testcases.CWE506_Embedded_Malicious_Code;
import testcasesupport.*;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
public class CWE506_Embedded_Malicious_Code__screen_capture_15 extends AbstractTestCaseBadOnly
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
                BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(100,100));
                ImageIO.write(screenCapture, "jpg", new File("C:/screen.jpg"));
            }
            catch (AWTException exceptAWT)
            {
                IO.logger.log(Level.WARNING, "Could not access screen for capture", exceptAWT);
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Could not access file system", exceptIO);
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
