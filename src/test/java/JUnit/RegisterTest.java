package JUnit;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.User;
import Service.Register;
import Service.Request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest
{
    private static final String USER_NAME = "jerbrown12";
    String requestBody = "{\n" +
            "\t\"username\":" + USER_NAME + ",\n" +
            "\t\"password\":\"password\",\n" +
            "\t\"email\":\"email\",\n" +
            "\t\"firstName\":\"firstname\",\n" +
            "\t\"lastName\":\"lastname\",\n" +
            "\t\"gender\":\"m/f\"\n" +
            "}";

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        Database db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }


    @Test
    public void addUserPass() throws DataAccessException
    {
        //create normal register request and use it to register user
        RegisterRequest request = new RegisterRequest(requestBody);
        Register register = new Register(request);
        request.createUser();
        register.addUser();

        //result username should match default username
        assertEquals(USER_NAME, register.result.getUsername());

        //test if user and person are in database. Should not throw errors.
        Database db = new Database();
        Connection conn  = db.getConn();
        db.userDAO = new UserDAO(conn);
        db.personDAO = new PersonDAO(conn);
        assertDoesNotThrow(()->db.userDAO.getUser(USER_NAME));
        assertDoesNotThrow(()->db.personDAO.getPersons(USER_NAME));
        db.closeConnection(false);
    }
    @Test
    public void addUserFail() throws DataAccessException
    {
        //try registering user, should work
        RegisterRequest request = new RegisterRequest(requestBody);
        Register register = new Register(request);
        request.createUser();
        register.addUser();

        //try registering user with same username again, should fail on addUser
        Register register2 = new Register(request);
        request.createUser();
        assertThrows(DataAccessException.class, ()->register2.addUser());
    }
}
