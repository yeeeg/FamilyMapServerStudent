package JUnit;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.User;
import Service.Fill;
import Service.Request.FillRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillTest
{
    Database db;
    final static String DEFAULT_REQUEST_BODY = "Request_Body";
    final static String DEFAULT_USER_NAME = "jerrybro";
    final static String DEFAULT_PASSWORD = "12345";
    final static String DEFAULT_PERSON_ID = "jeremiah_brown";
    final static String DEFAULT_FIRST_NAME = "jeremiah";
    final static String DEFAULT_LAST_NAME = "brown";
    final static int DEFAULT_GENERATIONS = 4;
    final static int DEFAULT_NUM_PEOPLE_GENERATIONS = 31;
    final static int DEFAULT_NUM_EVENTS_GENERATIONS = 91;

    FillRequest fillRequest;
    Fill fill;

    @BeforeEach
    public void setup() throws DataAccessException
    {
        db = new Database();
        Connection conn = db.getConn();
        //add user to database
        db.clearTables();
        User user = new User(DEFAULT_USER_NAME, DEFAULT_PASSWORD, "lost@sea.com", DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME,
                "m", DEFAULT_PERSON_ID);
        db.userDAO = new UserDAO(conn);
        db.userDAO.insert(user);
        db.closeConnection(true);
    }
    @AfterEach
    public void tearDown() throws DataAccessException
    {
        System.out.println("Cleanup called");
    }
    @Test
    public void doFillPass() throws DataAccessException
    {
        //initialize fillRequest with default data and create new fill object
        fillRequest = new FillRequest(DEFAULT_USER_NAME, DEFAULT_GENERATIONS);
        fill = new Fill(fillRequest);

        //do the fill, should not throw anything
        assertDoesNotThrow(()->fill.doFill());
        //test if fill created correct number of people
        assertEquals(DEFAULT_NUM_PEOPLE_GENERATIONS, fill.getPeople().size());
        //test if fill created correct number of events
        assertEquals(DEFAULT_NUM_EVENTS_GENERATIONS, fill.getEventsArray().size());
    }
    @Test
    public void doFillFail() throws DataAccessException
    {
        //initialize fillRequest with bad username and create new fill object
        fillRequest = new FillRequest("BAD_USERNAME", DEFAULT_GENERATIONS);
        fill = new Fill(fillRequest);

        //fill should fail because user "BAD_USERNAME" is not registered
        assertThrows(DataAccessException.class, ()->fill.doFill());
        //people count and event count should be their initialized values of 0
        assertEquals(0, fill.getPeople().size());
        assertEquals(0, fill.getEventsArray().size());
    }
}
