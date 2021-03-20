package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Service.Result.EventsResult;
import Service.Result.PersonsResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.util.ArrayList;

public class GetPersons
{
    public PersonsResult result;
    String username;
    Database db;
    public Gson gson;

    public GetPersons()
    {
        db = new Database();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }
    public void getAssociatedUN(String authtoken) throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();
            db.authTokenDAO = new AuthTokenDAO(connection);
            AuthToken auth = db.authTokenDAO.find(authtoken);
            db.closeConnection(true);
            username = auth.getUsername();
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
            result = new PersonsResult(e.getMessage(), false);
            throw new DataAccessException(e.getMessage());
        }
    }
    public void doGet() throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();

            //get eventID associated event object from db
            db.personDAO = new PersonDAO(connection);
            ArrayList<Person> persons = db.personDAO.getPersons(username);
            db.closeConnection(true);
            result = new PersonsResult(persons, true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);

            result = new PersonsResult(e.getMessage(), false);
            throw new DataAccessException(e.getMessage());
        }
    }
}
