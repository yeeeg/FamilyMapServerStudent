package Service;

import DAO.*;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.sql.Connection;

/**
 * Clears all data from the database (just like the /clear API),
 * and then loads the posted user, person, and event data into the database.
 *
 * HTTP Method: POST
 * Auth Token Required: No
 *
 * Errors: Invalid request data (missing values, invalid values, etc.), Internal server error
 *
 */
public class Load
{
    public LoadResult result;
    LoadRequest request;
    Database db;
    public Gson gson;

    /**
     * Constructor for Load
     * @param request the provided request body
     */
    public Load(LoadRequest request)
    {
        this.request = request;
        this.db = new Database();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gson = gsonBuilder.create();
    }

    /**
     * Loads all of the provided data into the database
     * @throws DataAccessException Issues accessing database
     */
    public void doLoad() throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();
            db.userDAO = new UserDAO(connection);
            db.personDAO = new PersonDAO(connection);
            db.eventDAO = new EventDAO(connection);

            for (int i = 0; i < request.loadModel.users.size(); ++i)
            {
                db.userDAO.insert(request.loadModel.users.get(i));
            }
            for (int i = 0; i < request.loadModel.persons.size(); ++i)
            {
                db.personDAO.insert(request.loadModel.persons.get(i));
            }
            for (int i = 0; i < request.loadModel.events.size(); ++i)
            {
                db.eventDAO.insert(request.loadModel.events.get(i));
            }

            db.closeConnection(true);

            result = new LoadResult("Successfully added " + request.loadModel.users.size() +
                    " users, " + request.loadModel.persons.size() + " persons, and " +
                    request.loadModel.events.size() + " events to the database.", true);

        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
            result = new LoadResult(e.getMessage(), false);
        }
    }
}
