package Service;

import DAO.*;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.sql.Connection;

/**
 * URL Path: /load
 * Description: Clears all data from the database (just like the /clear API),
 * and then loads the posted user, person, and event data into the database.
 *
 * HTTP Method: POST
 * Auth Token Required: No
 *
 * Request Body: The “users” property in the request body contains an array of users to be created.
 * The “persons” and “events” properties contain family history information for these users.
 * The objects contained in the “persons” and “events” arrays should be added to the server’s database.
 * The objects in the “users” array have the same format as those passed to the /user/register API
 * with the addition of the personID. The objects in the “persons” array have the same format as those
 * returned by the /person/[personID] API. The objects in the “events” array have the same format as those
 * returned by the /event/[eventID] API.
 * {
 * 	    “users”: [ Array of User objects ],
 *      “persons”:[ Array of Person objects ],
 *      “events”:[ Array of Event objects ]
 * }
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

    public Load(LoadRequest request)
    {
        this.request = request;
        this.db = new Database();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gson = gsonBuilder.create();
    }
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
