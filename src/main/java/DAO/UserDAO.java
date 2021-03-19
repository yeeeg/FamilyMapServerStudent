package DAO;
import java.io.File;
import java.sql.*;
import Model.*;
import Service.Result.RegisterResult;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {
    Connection connection;

    public UserDAO(Connection connection)
    {
        this.connection = connection;
    }

    public void clear()
    {

    }

    /**
     * Simply returns whether or not the provided username exists in the db
     * @param username Username string
     * @return whether or not username is in db
     */
    public boolean contains(String username) throws DataAccessException
    {
        boolean result;
        ResultSet rs;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            result = rs.next();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error finding username \"" + username + "\" in users table.");
        }
        return result;
    }

    /**
     * Finds user in database. If user doesn't exist, throws error.
     * @param user provided user object that contains username and password
     * @return Authtoken object
     * @throws DataAccessException User DNE/Incorrect password
     */
    public AuthToken login(User user) throws DataAccessException
    {
        AuthToken authtoken = new AuthToken();
        String sql = "SELECT * FROM users WHERE username = ?;";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, user.getUsername());
            rs = stmt.executeQuery();
            if (rs.getString("password").equals(user.getPassword()))
            {
                authtoken.setAuthtoken(UUID.randomUUID().toString());
                authtoken.setUsername(user.getUsername());
                authtoken.setPassword(user.getPassword());
                authtoken.setPersonID((user.getPersonID()));
            }
            else
            {
                throw new DataAccessException("Incorrect Password");
            }
        }
        catch(SQLException e)
        {
            throw new DataAccessException("User does not exist");
        }
        return authtoken;
    }
    public void insert(User user) throws DataAccessException
    {
        String sql = "INSERT INTO users (username, password, email, first_name, last_name, gender, person_id) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error encountered while inserting User into the database");
        }
    }
    private void update(String sql)
    {
    }
    public void delete(String username) throws DataAccessException
    {
        String sql = "DELETE FROM users WHERE username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error deleting from users");
        }
    }

    public User getUser(String username) throws DataAccessException
    {
        User user;
        String sql = "SELECT * FROM users WHERE username = ?;";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("gender"),
                        rs.getString("person_id"));
            }
            else
            {
                throw new DataAccessException("User does not exist.");
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

        return user;
    }
}
