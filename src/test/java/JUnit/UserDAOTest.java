package JUnit;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest
{
    private static final String USER_NAME = "jerbrown12";
    private static final String OTHER_USER_NAME = "yeet";
    private Database db;
    private User u;
    private User u2;
    private User u3;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        Connection conn = db.getConn();

        //main user
        u = new User(USER_NAME, "12345", "jerbown12@gmail.com", "jeremiah", "brown",
                "m", "jb12345");

        //other user
        u2 = new User(OTHER_USER_NAME, "12345", "jerbown12@gmail.com", "jeremiah", "brown",
                "m", "jb12345");

        db.clearTables();
        userDAO = new UserDAO(conn);
    }
    @AfterEach
    public void tearDown() throws DataAccessException
    {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException
    {
        userDAO.insert(u);
        //get user we just inserted from db
        User compareTest = userDAO.getUser(u.getUsername());
        //check if we found anything
        assertNotNull(compareTest);
        //check if what we found is the same as what we put in
        assertEquals(u, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException
    {
        //test a second time but make it fail instead
        userDAO.insert(u);
        //assert that our insert fails the second time when we try to insert the same object
        assertThrows(DataAccessException.class, ()-> userDAO.insert(u));
    }

    @Test
    public void getUserPass() throws DataAccessException
    {
        userDAO.insert(u);
        userDAO.insert(u2);

        //get user back from db
        User compareTest = userDAO.getUser(u.getUsername());

        //objects should match if getUser worked
        assertEquals(u, compareTest);
    }
    @Test
    public void getUserFail() throws DataAccessException
    {
        userDAO.insert(u);

        //try to get user u2 back from db, should fail since it is not in db

        assertThrows(DataAccessException.class, ()->userDAO.getUser(u2.getUsername()));
    }

    @Test
    public void containsPass() throws DataAccessException
    {
        userDAO.insert(u);
        //contains should return true for user u
        assertTrue(userDAO.contains(u.getUsername()));
    }
    @Test
    public void containsFail() throws DataAccessException
    {
        userDAO.insert(u);
        userDAO.delete(u.getUsername());

        //should return false because u was just deleted from db
        assertFalse(userDAO.contains(u.getUsername()));
    }

    @Test
    public void deletePass() throws DataAccessException
    {
        userDAO.insert(u);
        userDAO.insert(u2);

        userDAO.delete(u2.getUsername());

        //should be able to reinsert u2 into database because no duplicate exists
        assertDoesNotThrow(()->userDAO.insert(u2));
    }
    @Test
    public void deleteFail() throws DataAccessException
    {
        userDAO.insert(u);
        userDAO.insert(u2);

        userDAO.delete(u2.getUsername());

        //should not be able to find u2 in database because it was just deleted
        assertThrows(DataAccessException.class, ()->userDAO.getUser(u2.getUsername()));
    }

    @Test
    public void loginPass() throws DataAccessException
    {
        userDAO.insert(u);
        userDAO.insert(u2);
        AuthToken authToken = new AuthToken("123456", USER_NAME, "12345");
        //check everything is the same in authtoken object except for the actual authtoken
        assertEquals(authToken.getUsername(), userDAO.login(u).getUsername());
        assertEquals(authToken.getPassword(), userDAO.login(u).getPassword());
        //should be able to login because user is in database
        assertDoesNotThrow(()->userDAO.login(u));
    }
    @Test
    public void loginFail() throws DataAccessException
    {
        userDAO.insert(u);

        //should not be able to login user u2 because it is not in the database
        assertThrows(DataAccessException.class, ()->userDAO.login(u2));
    }
}
