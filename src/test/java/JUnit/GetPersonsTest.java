package JUnit;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Service.GetEvents;
import Service.GetPersons;
import Service.Login;
import Service.Request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetPersonsTest
{
    GetPersons getPersons;
    Database db;
    private String authToken;
    String EVENT_ID1 = "1234567890";
    String EVENT_ID2 = "0987654321";
    ArrayList<Person> persons;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //this set up essentially does what RegisterHandler does
        //clear tables
        db = new Database();
        Connection conn = db.openConnection();
        db.clearTables();

        //open daos
        db.eventDAO = new EventDAO(conn);
        db.personDAO = new PersonDAO(conn);
        db.userDAO = new UserDAO(conn);
        db.authTokenDAO = new AuthTokenDAO(conn);

        //add a person to db
        Person person = new Person("jb12345", "jerbrown12", "jeremiah",
                "brown", "m", "alkjsldkj", "lkdjfl", "asdf");
        db.personDAO.insert(person);

        //add a user to db
        User user = new User("jerbrown12", "12345", "lost@sea.com", "jeremiah",
                "brown", "m", "jb12345");
        db.userDAO.insert(user);

        //create a couple events for user
        Event testEvent1 = new Event(EVENT_ID1, "jerbrown12", "jb12345",
                21.3f, 43.2f, "America", "Provo", "Birth", 1997);

        Event testEvent2 = new Event(EVENT_ID2, "jerbrown12", "jb12345",
                21.3f, 43.2f, "America", "Provo", "Marriage", 2022);
        db.eventDAO.insert(testEvent1);
        db.eventDAO.insert(testEvent2);

        persons = new ArrayList<>();
        persons.add(person);

        //commit changes
        db.closeConnection(true);

        //log user in
        LoginRequest loginRequest = new LoginRequest(user);
        Login login = new Login(loginRequest);
        login.loginUser();

        //assign authtoken to local variable
        authToken = login.result.getAuthtoken();
        getPersons = new GetPersons();
    }

    @Test
    public void getAssociatedUNPass() throws DataAccessException
    {
        //check if username is actually associated with created auth key
        getPersons.getAssociatedUN(authToken);
        assertEquals("jerbrown12", getPersons.username);
    }
    @Test
    public void getAssociatedUNFail() throws DataAccessException
    {
        //gives a bad authtoken. Method should throw an error because auth DNE
        assertThrows(DataAccessException.class, ()->getPersons.getAssociatedUN("BAD_TOKEN"));
    }
    @Test
    public void doGetPass() throws DataAccessException
    {
        //get associated un
        getPersons.getAssociatedUN(authToken);
        //do get
        getPersons.doGet();

        //compare event arrays, they should be equal
        assertEquals(getPersons.result.data, persons);
    }
    @Test
    public void doGetFail() throws DataAccessException
    {
        //do get without first calling getAssociatedUN, should throw error
        assertThrows(DataAccessException.class, ()->getPersons.doGet());
    }
}
