package Service;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;

import Service.Request.*;
import Service.Result.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.util.UUID;

public class Register {
    public RegisterResult result;
    RegisterRequest request;
    Database db;
    public Gson gson;

    /**
     * Constructor for Register
     * @param request Request body string
     */
    public Register(RegisterRequest request)
    {
        this.request = request;
        this.db = new Database();
    }

    /**
     * Adds user and associated person to the database
     * @throws DataAccessException Issues with database access
     */
    public void addUser() throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();

            db.personDAO = new PersonDAO(connection);
            db.personDAO.insert(request.person);

            db.userDAO = new UserDAO(connection);
            db.userDAO.insert(request.user);
            db.closeConnection(true);

            result = new RegisterResult();

            //will assign authtoken after login
            result.success(request.user.getUsername(), request.user.getPersonID());

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();

            gson = gsonBuilder.create();
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gson = gsonBuilder.create();

            result = new RegisterResult();
            result.failure("Error inserting User/Person into database from Register.addUser()");
            throw new DataAccessException("Error inserting User/Person into database from Register.addUser()");
        }
    }
}
