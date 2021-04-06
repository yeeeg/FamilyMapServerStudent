package JUnit;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.AuthToken;
import Service.Login;
import Service.Register;
import Service.Request.LoginRequest;
import Service.Request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest
{
    Database db;
    private final static String USER_NAME = "jerbrown12";
    String requestBody = "{\n" +
            "\t\"username\":"+ USER_NAME + ",\n" +
            "\t\"password\":\"password\"\n" +
            "}";

    String registerBody = "{\n" +
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
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
        Connection conn = db.openConnection();

        db.userDAO = new UserDAO(conn);
        db.personDAO = new PersonDAO(conn);
    }
    public void tearDown() throws DataAccessException
    {
        db.closeConnection(false);
    }

    @Test
    public void loginUserPass() throws DataAccessException
    {
        //first register new user
        RegisterRequest request = new RegisterRequest(registerBody);
        Register register = new Register(request);
        request.createUser();
        register.addUser();

        //create login request and attempt to login user
        LoginRequest loginRequest = new LoginRequest(requestBody);
        Login login = new Login(loginRequest);
        loginRequest.createUserObject();
        //log user in, shouldn't throw anything
        assertDoesNotThrow(()->login.loginUser());
    }
    @Test
    public void loginUserFail() throws DataAccessException
    {
        //this time don't register a user

        //create login request and attempt to login user. Should fail because user was not already registered.
        LoginRequest loginRequest = new LoginRequest(requestBody);
        Login login = new Login(loginRequest);
        loginRequest.createUserObject();
        //should fail
        assertThrows(DataAccessException.class, ()->login.loginUser());
    }
}
