package JUnit;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Service.GetEvent;
import Service.GetPerson;
import Service.Login;
import Service.Request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetPersonTest
{
    Database db;
    private String authToken;
    String PERSON_ID1 = "1234567890";
    String PERSON_ID2 = "0987654321";

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
        Person person = new Person(PERSON_ID1, "jerbrown12", "jeremiah",
                "brown", "m", "alkjsldkj", "lkdjfl", "asdf");
        db.personDAO.insert(person);

        //add a user to db
        User user = new User("jerbrown12", "12345", "lost@sea.com", "jeremiah",
                "brown", "m", PERSON_ID1);
        db.userDAO.insert(user);

        //create a couple events for user
        Event testEvent1 = new Event("event1", "jerbrown12", PERSON_ID1,
                21.3f, 43.2f, "America", "Provo", "Birth", 1997);

        Event testEvent2 = new Event("event2", "jerbrown12", PERSON_ID1,
                21.3f, 43.2f, "America", "Provo", "Marriage", 2022);
        db.eventDAO.insert(testEvent1);
        db.eventDAO.insert(testEvent2);

        //commit changes
        db.closeConnection(true);

        //log user in
        LoginRequest loginRequest = new LoginRequest(user);
        Login login = new Login(loginRequest);
        login.loginUser();

        //assign authtoken to local variable
        authToken = login.result.getAuthtoken();
    }

    @Test
    public void checkAuthPass() throws DataAccessException
    {
        //initialize getPerson
        GetPerson getPerson = new GetPerson(PERSON_ID1);

        //checkAuth should not throw any errors with specified authtoken
        assertDoesNotThrow(()->getPerson.checkAuth(authToken));
    }
    @Test
    public void checkAuthFail() throws DataAccessException
    {
        //initialize getPerson
        GetPerson getPerson = new GetPerson(PERSON_ID1);
        //should throw error because provided authtoken does not exist
        assertThrows(DataAccessException.class, ()->getPerson.checkAuth("BAD_TOKEN"));
    }
}
