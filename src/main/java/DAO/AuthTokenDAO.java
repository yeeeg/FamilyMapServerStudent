package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.*;

public class AuthTokenDAO {
    Connection connection;

    public AuthTokenDAO(Connection connection)
    {
        this.connection = connection;
    }

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
    public AuthToken find(String authtoken)
    {
        //TODO:ADD THIS METHOD

        return null;
    }
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
