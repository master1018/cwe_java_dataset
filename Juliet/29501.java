
package testcases.CWE89_SQL_Injection.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
public class CWE89_SQL_Injection__getCookies_Servlet_executeUpdate_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE89_SQL_Injection__getCookies_Servlet_executeUpdate_68a.data;
        Connection dbConnection = null;
        Statement sqlStatement = null;
        try
        {
            dbConnection = IO.getDBConnection();
            sqlStatement = dbConnection.createStatement();
            int rowCount = sqlStatement.executeUpdate("insert into users (status) values ('updated') where name='"+data+"'");
            IO.writeLine("Updated " + rowCount + " rows successfully.");
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
        }
        finally
        {
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
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE89_SQL_Injection__getCookies_Servlet_executeUpdate_68a.data;
        Connection dbConnection = null;
        Statement sqlStatement = null;
        try
        {
            dbConnection = IO.getDBConnection();
            sqlStatement = dbConnection.createStatement();
            int rowCount = sqlStatement.executeUpdate("insert into users (status) values ('updated') where name='"+data+"'");
            IO.writeLine("Updated " + rowCount + " rows successfully.");
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
        }
        finally
        {
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
    }
    public void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE89_SQL_Injection__getCookies_Servlet_executeUpdate_68a.data;
        Connection dbConnection = null;
        PreparedStatement sqlStatement = null;
        try
        {
            dbConnection = IO.getDBConnection();
            sqlStatement = dbConnection.prepareStatement("insert into users (status) values ('updated') where name=?");
            sqlStatement.setString(1, data);
            int rowCount = sqlStatement.executeUpdate();
            IO.writeLine("Updated " + rowCount + " rows successfully.");
        }
        catch (SQLException exceptSql)
        {
            IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
        }
        finally
        {
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
    }
}
