package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.*;

public class AuthTokenDAO {
    Connection connection;

    /**
     * Constructor for AuthTokenDAO
     * @param connection the provided connection to the database
     */
    public AuthTokenDAO(Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Inserts an authToken object into the database
     * @param authToken authToken object
     * @throws DataAccessException Error thrown when SQLException is thrown
     */
    public void insert(AuthToken authToken) throws DataAccessException
    {
        String sql = "INSERT INTO auth_tokens (auth_token, a_UN, password) VALUES(?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, authToken.getAuthtoken());
            stmt.setString(2, authToken.getUsername());
            stmt.setString(3, authToken.getPassword());

            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new DataAccessException("Error encountered while inserting authtoken into the database " + e.toString());
        }
    }

    /**
     * Finds the AuthToken object associated with the authtoken string and returns
     * an AuthToken object
     * @param authtoken Provided authtoken string
     * @return AuthToken object
     * @throws DataAccessException Error thrown when authtoken is invalid or
     * SQLException is thrown
     */
    public AuthToken find(String authtoken) throws DataAccessException
    {
        AuthToken authToken;
        String sql = "SELECT * FROM auth_tokens WHERE auth_token = ?;";
        ResultSet rs = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, authtoken);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                authToken = new AuthToken(rs.getString("auth_token"),
                        rs.getString("a_UN"), rs.getString("password"));
            }
            else
            {
                throw new DataAccessException("Error: Invalid auth token.");
            }
        }
        catch(SQLException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return authToken;
    }

    /**
     * Deletes the specified user out of the auth_token table
     * @param username Provided username
     * @throws DataAccessException Error when SQLException is thrown
     */
    public void delete(String username) throws DataAccessException
    {
        String sql = "DELETE FROM auth_tokens WHERE a_UN = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error deleting from auth_tokens");
        }
    }
}
