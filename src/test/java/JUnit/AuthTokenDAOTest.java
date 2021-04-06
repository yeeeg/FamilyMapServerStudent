package JUnit;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.AuthToken;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest
{
    private static final String USER_NAME = "jerbrown12";
    private static final String OTHER_USER_NAME = "yeet";
    private Database db;
    private AuthToken at;
    private AuthToken at2;
    private AuthToken at3;
    private AuthToken at4;
    private AuthTokenDAO authTokenDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        Connection conn = db.getConn();

        at = new AuthToken("TEST_AUTHTOKEN_1", USER_NAME, "12345");
        at2 = new AuthToken("TEST_AUTHTOKEN_2", USER_NAME, "12345");
        at3 = new AuthToken("TEST_AUTHTOKEN_3", USER_NAME, "12345");

        //alternate user authtoken
        at4 = new AuthToken("TEST_AUTHTOKEN_4", OTHER_USER_NAME, "67890");

        db.clearTables();
        authTokenDAO = new AuthTokenDAO(conn);
    }
    @AfterEach
    public void tearDown() throws DataAccessException
    {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException
    {
        authTokenDAO.insert(at);
        //get authToken we just inserted from db
        AuthToken compareTest = authTokenDAO.find(at.getAuthtoken());
        //check if we found anything
        assertNotNull(compareTest);
        //check if what we found is the same as what we put in
        assertEquals(at, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException
    {
        //test a second time but make it fail instead
        authTokenDAO.insert(at);
        //assert that our insert fails the second time, we try to insert the same object
        assertThrows(DataAccessException.class, ()-> authTokenDAO.insert(at));
    }

    @Test
    public void findPass() throws DataAccessException
    {
        //insert auth tokens into db
        authTokenDAO.insert(at);
        authTokenDAO.insert(at2);
        //find one of the auth token inside of db
        assertDoesNotThrow(()-> authTokenDAO.find(at2.getAuthtoken()));
    }
    @Test
    public void findFail() throws DataAccessException
    {
        //insert auth tokens into db
        authTokenDAO.insert(at);
        authTokenDAO.insert(at2);
        authTokenDAO.insert(at3);
        //find authToken that does not exist inside db
        assertThrows(DataAccessException.class, ()->authTokenDAO.find("INVALID_TOKEN"));
    }

    @Test
    public void deletePass() throws DataAccessException
    {
        //insert auth tokens into db
        authTokenDAO.insert(at);
        authTokenDAO.insert(at2);
        authTokenDAO.insert(at3);
        //delete authToken at2 from db
        authTokenDAO.delete((at2.getUsername()));
        //we should not be able to reinsert the authtoken since there wont be a duplicate
        assertDoesNotThrow(()->authTokenDAO.insert(at2));
    }
    @Test
    public void deleteFail() throws DataAccessException
    {
        //insert auth tokens into db
        authTokenDAO.insert(at);
        authTokenDAO.insert(at2);
        authTokenDAO.insert(at3);
        //delete authToken at2 from db
        authTokenDAO.delete(at2.getUsername());
        //at2 should not be found
        assertThrows(DataAccessException.class, ()->authTokenDAO.find(at2.getAuthtoken()));
    }
}
