package JUnit;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.AuthToken;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest
{
    private static final String USER_NAME = "jerbrown12";
    private static final String OTHER_USER_NAME = "yeet";
    private Database db;
    private PersonDAO personDAO;
    private Person p;
    private Person p2;
    private Person p3;
    private Person p4;
    private ArrayList<Person> persons;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
        Connection conn = db.getConn();

        p = new Person("jb12345", USER_NAME, "jeremiah", "brown", "m",
                "asdf", "jkl", "hs12345");
        p2 = new Person("hs12345", USER_NAME, "heather", "arnolds", "f",
                "qwerty", "uioup", "jb12345");

        //same as p2 but with different username and personID
        p3 = new Person("hs123456", OTHER_USER_NAME, "heather", "arnolds", "f",
                "qwerty", "uioup", "jb12345");
        db.clearTables();
        personDAO = new PersonDAO(conn);
        persons = new ArrayList<>();
    }
    @AfterEach
    public void tearDown() throws DataAccessException
    {
        db.closeConnection(false);
        persons.clear();
    }

    @Test
    public void insertPass() throws DataAccessException
    {
        personDAO.insert(p);
        //get person we just inserted from db
        Person compareTest = personDAO.getPerson(p.getPersonID());
        //check if we found anything
        assertNotNull(compareTest);
        //check if what we found is the same as what we put in
        assertEquals(p, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException
    {
        //test a second time but make it fail instead
        personDAO.insert(p);
        //assert that our insert fails the second time, we try to insert the same object
        assertThrows(DataAccessException.class, ()-> personDAO.insert(p));
    }

    @Test
    public void getPersonPass() throws DataAccessException
    {
        //insert person into db
        personDAO.insert(p);
        personDAO.insert(p2);

        //find p2 from db
        Person compareTest = personDAO.getPerson(p2.getPersonID());
        assertEquals(p2, compareTest);
    }
    @Test
    public void getPersonFail() throws DataAccessException
    {
        //insert person into db
        personDAO.insert(p2);

        //try to find p from db (should not exist)
        assertThrows(DataAccessException.class, ()->personDAO.getPerson(p.getPersonID()));
    }

    @Test
    public void getPersonsPass() throws DataAccessException
    {
        //insert persons into db
        personDAO.insert(p);
        personDAO.insert(p2);
        personDAO.insert(p3);

        //insert persons into arrayList, except for p3 because it differs from the others
        persons.add(p);
        persons.add(p2);

        //check if getPersons returns arrayList equal to array list we just filled

        ArrayList<Person> result = personDAO.getPersons(p.getAssociatedUsername());
        assertEquals(persons, result);
    }
    @Test
    public void getPersonsFail() throws DataAccessException
    {
        //insert persons into db
        personDAO.insert(p);
        personDAO.insert(p2);
        personDAO.insert(p3);

        //check for bad username, getPersons should fail as a result
        assertThrows(DataAccessException.class, ()->personDAO.getPersons("BAD_UN"));
    }

    @Test
    public void deletePass() throws DataAccessException
    {
        //insert persons into db
        personDAO.insert(p);
        personDAO.insert(p2);
        personDAO.insert(p3);

        //delete p and p2 out of db since they share username
        personDAO.delete(USER_NAME);

        //should be able to re insert p and/or p2 into database
        assertDoesNotThrow(()->personDAO.insert(p));
        assertDoesNotThrow(()->personDAO.insert(p2));
    }
    @Test
    public void deleteFail() throws DataAccessException
    {
        //insert persons into db
        personDAO.insert(p);
        personDAO.insert(p2);
        personDAO.insert(p3);

        //delete p from db
        personDAO.delete(p.getAssociatedUsername());

        //p should not be found in db
        assertThrows(DataAccessException.class, ()->personDAO.getPerson(p.getPersonID()));
    }
}
