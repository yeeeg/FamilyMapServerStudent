package DAO;

import Model.AuthToken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class Database
{
    private Connection conn;
    public AuthTokenDAO authTokenDAO;
    public EventDAO eventDAO;
    public PersonDAO personDAO;
    public UserDAO userDAO;
    public String authtoken;

    public Connection openConnection() throws DataAccessException
    {
        try
        {
            final String url = "jdbc:sqlite:FamilyMap.sqlite";

            conn = DriverManager.getConnection(url);

            conn.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public Connection getConn() throws DataAccessException
    {
        if (conn == null)
        {
            return openConnection();
        }
        else
        {
            return conn;
        }
    }

    public void closeConnection(boolean commit) throws DataAccessException
    {
        try
        {
            if (commit) conn.commit();
            else conn.rollback();

            conn.close();
            conn = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void clearTables() throws DataAccessException
    {
        try (Statement stmt = conn.createStatement())
        {
            String sql = "DELETE FROM users";
            stmt.executeUpdate(sql);

            sql = "DELETE FROM persons";
            stmt.executeUpdate(sql);

            sql = "DELETE FROM events";
            stmt.executeUpdate(sql);

            sql = "DELETE FROM auth_tokens";
            stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }

    }

    public String genAuthtoken()
    {
        return UUID.randomUUID().toString();
    }

}
