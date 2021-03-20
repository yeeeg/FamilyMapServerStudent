package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Service.Result.EventResult;
import Service.Result.RegisterResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.crypto.Data;
import java.sql.Connection;

public class GetEvent
{
    public EventResult result;
    String eventID;
    Database db;
    public Gson gson;

    public GetEvent(String eventID)
    {
        this.eventID = eventID;
        db = new Database();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    public void checkAuth(String authtoken) throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();
            db.authTokenDAO = new AuthTokenDAO(connection);
            AuthToken auth = db.authTokenDAO.find(authtoken);

            //if we've made it this far, authtoken exists inside of db
            //now we need to check and see if eventID belongs to the user
            //referenced in AuthToken auth object

            //get eventID associated event object from db
            db.eventDAO = new EventDAO(connection);
            Event event = db.eventDAO.find(eventID);

            if (event.getAssociatedUsername().equals(auth.getUsername()))
            {
                db.closeConnection(true);
                result = new EventResult(event, true);
            }
            else
            {
                throw new DataAccessException("Error: Requested event does not belong to this user.");
            }
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);

            result = new EventResult(e.getMessage(), false);
            throw new DataAccessException(e.getMessage());
        }
    }

}
