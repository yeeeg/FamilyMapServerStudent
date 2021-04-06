package JUnit;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest
{
    private static final String USER_NAME = "jerbrown12";
    private static final String OTHER_USER_NAME = "yeet";
    private Database db;
    private Event event;
    private Event event2;
    private Event event3;
    private Event event4;
    private EventDAO eventDAO;
    private ArrayList<Event> events;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        event = new Event("Car_Crash2013", USER_NAME, "jb12345",
                12.9f, 130.4f, "America", "Roseville", "Car_Accident",
                2013);

        event2 = new Event("JBirth1997", USER_NAME, "jb12345",
            12.9f, 130.4f, "America", "Provo", "Birth",
            1997);

        event3 = new Event("JMarriage2022", USER_NAME, "jb12345",
                12.9f, 130.4f, "America", "Provo", "Marriage",
                2022);

        //event with different username
        event4 = new Event("NewCar2022", OTHER_USER_NAME, "jb12345",
                12.9f, 130.4f, "America", "Provo", "New_Car",
                2022);

        events = new ArrayList<>();
        Connection conn = db.getConn();
        db.clearTables();
        eventDAO = new EventDAO(conn);
    }
    @AfterEach
    public void tearDown() throws DataAccessException
    {
        db.closeConnection(false);
        events.clear();
    }

    @Test
    public void insertPass() throws DataAccessException
    {
        eventDAO.insert(event);
        //get event we just inserted from db
        Event compareTest = eventDAO.find(event.getEventID());
        //check if we found anything
        assertNotNull(compareTest);
        //check if what we found is the same as what we put in
        assertEquals(event, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException
    {
        //test a second time but make it fail instead
        eventDAO.insert(event);
        //assert that our insert fails the second time we try to insert the same object
        assertThrows(DataAccessException.class, ()-> eventDAO.insert(event));
    }

    @Test
    public void getEventsPass() throws DataAccessException
    {
        //add events to database
        eventDAO.insert(event);
        eventDAO.insert(event2);
        eventDAO.insert(event3);

        //add events to ArrayList
        events.add(event);
        events.add(event2);
        events.add(event3);

        //get events back from database and see if they match
        ArrayList<Event> returnList = eventDAO.getEvents(USER_NAME);
        assertEquals(events, returnList);
    }
    @Test
    public void getEventsFail() throws DataAccessException
    {
        eventDAO.insert(event);
        eventDAO.insert(event2);
        eventDAO.insert(event3);
        eventDAO.insert(event4);

        //insert all event objects into arraylist, despite different usernames
        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        ArrayList<Event> returnList = eventDAO.getEvents(USER_NAME);
        //array lists should not equal each other, as the original array contains multiple user names
        assertNotEquals(events, returnList);
    }

    @Test
    public void findPass() throws DataAccessException {
        //insert event into the db
        eventDAO.insert(event);
        //get object from db using find
        Event result = eventDAO.find(event.getEventID());
        //objects should equal each other
        assertEquals(event, result);
    }
    @Test
    public void findFail() throws DataAccessException
    {
        //insert event2 into database
        eventDAO.insert(event2);
        //try to get different object from database
        assertThrows(DataAccessException.class, ()-> eventDAO.find(event.getEventID()));
    }

    @Test
    public void deletePass() throws DataAccessException {
        //insert item into db
        eventDAO.insert(event);
        //delete item from db
        eventDAO.delete(event.getAssociatedUsername());
        //should be able to insert same item back into database without error
        assertDoesNotThrow(()-> eventDAO.insert(event));
    }
    @Test
    public void deleteFail() throws DataAccessException
    {
        //insert item into db
        eventDAO.insert(event);
        //delete item from db
        eventDAO.delete(event.getAssociatedUsername());
        //make sure item is not in database
        assertThrows(DataAccessException.class, ()-> eventDAO.find(event.getEventID()));
    }
}
