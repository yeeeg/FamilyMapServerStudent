package Service;
import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import Service.Result.*;
import Model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Handles both /event and /event/[eventID], depending on construction params
 */
public class GetEvents
{
    public EventsResult result;
    String username;
    Database db;
    public Gson gson;

    public GetEvents()
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
            result = new EventsResult(e.getMessage(), false);
            throw new DataAccessException(e.getMessage());
        }
    }
    public void doGet() throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();

            //get eventID associated event object from db
            db.eventDAO = new EventDAO(connection);
            ArrayList<Event> events = db.eventDAO.getEvents(username);
            db.closeConnection(true);
            result = new EventsResult(events, true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);

            result = new EventsResult(e.getMessage(), false);
            throw new DataAccessException(e.getMessage());
        }
    }
}
