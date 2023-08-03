
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__database_HashMap_22b
{
    public int badSource() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__database_HashMap_22a.badPublicStatic)
        {
            data = Integer.MIN_VALUE; 
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try
                {
                    connection = IO.getDBConnection();
                    preparedStatement = connection.prepareStatement("select name from users where id=0");
                    resultSet = preparedStatement.executeQuery();
                    String stringNumber = resultSet.getString(1);
                    if (stringNumber != null) 
                    {
                        try
                        {
                            data = Integer.parseInt(stringNumber.trim());
                        }
                        catch (NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                        }
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error with SQL statement", exceptSql);
                }
                finally
                {
                    try
                    {
                        if (resultSet != null)
                        {
                            resultSet.close();
                        }
                    }
                    catch (SQLException exceptSql)
                    {
                        IO.logger.log(Level.WARNING, "Error closing ResultSet", exceptSql);
                    }
                    try
                    {
                        if (preparedStatement != null)
                        {
                            preparedStatement.close();
                        }
                    }
                    catch (SQLException exceptSql)
                    {
                        IO.logger.log(Level.WARNING, "Error closing PreparedStatement", exceptSql);
                    }
                    try
                    {
                        if (connection != null)
                        {
                            connection.close();
                        }
                    }
                    catch (SQLException exceptSql)
                    {
                        IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                    }
                }
            }
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public int goodG2B1Source() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__database_HashMap_22a.goodG2B1PublicStatic)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    public int goodG2B2Source() throws Throwable
    {
        int data;
        if (CWE789_Uncontrolled_Mem_Alloc__database_HashMap_22a.goodG2B2PublicStatic)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
}
