
package testcases.CWE89_SQL_Injection.s04;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
public class CWE89_SQL_Injection__Property_executeQuery_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = System.getProperty("user.home");
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            Connection dbConnection = null;
            Statement sqlStatement = null;
            ResultSet resultSet = null;
            try
            {
                dbConnection = IO.getDBConnection();
                sqlStatement = dbConnection.createStatement();
                resultSet = sqlStatement.executeQuery("select * from users where name='"+data+"'");
                IO.writeLine(resultSet.getRow()); 
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
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
                    if (sqlStatement != null)
                    {
                        sqlStatement.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Statement", exceptSql);
                }
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "foo";
            break;
        }
        switch (7)
        {
        case 7:
            Connection dbConnection = null;
            Statement sqlStatement = null;
            ResultSet resultSet = null;
            try
            {
                dbConnection = IO.getDBConnection();
                sqlStatement = dbConnection.createStatement();
                resultSet = sqlStatement.executeQuery("select * from users where name='"+data+"'");
                IO.writeLine(resultSet.getRow()); 
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
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
                    if (sqlStatement != null)
                    {
                        sqlStatement.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Statement", exceptSql);
                }
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = "foo";
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            Connection dbConnection = null;
            Statement sqlStatement = null;
            ResultSet resultSet = null;
            try
            {
                dbConnection = IO.getDBConnection();
                sqlStatement = dbConnection.createStatement();
                resultSet = sqlStatement.executeQuery("select * from users where name='"+data+"'");
                IO.writeLine(resultSet.getRow()); 
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
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
                    if (sqlStatement != null)
                    {
                        sqlStatement.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Statement", exceptSql);
                }
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = System.getProperty("user.home");
            break;
        default:
            data = null;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            Connection dbConnection = null;
            PreparedStatement sqlStatement = null;
            ResultSet resultSet = null;
            try
            {
                dbConnection = IO.getDBConnection();
                sqlStatement = dbConnection.prepareStatement("select * from users where name=?");
                sqlStatement.setString(1, data);
                resultSet = sqlStatement.executeQuery();
                IO.writeLine(resultSet.getRow()); 
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
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
                    if (sqlStatement != null)
                    {
                        sqlStatement.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing PreparedStatement", exceptSql);
                }
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        switch (6)
        {
        case 6:
            data = System.getProperty("user.home");
            break;
        default:
            data = null;
            break;
        }
        switch (7)
        {
        case 7:
            Connection dbConnection = null;
            PreparedStatement sqlStatement = null;
            ResultSet resultSet = null;
            try
            {
                dbConnection = IO.getDBConnection();
                sqlStatement = dbConnection.prepareStatement("select * from users where name=?");
                sqlStatement.setString(1, data);
                resultSet = sqlStatement.executeQuery();
                IO.writeLine(resultSet.getRow()); 
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
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
                    if (sqlStatement != null)
                    {
                        sqlStatement.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing PreparedStatement", exceptSql);
                }
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
