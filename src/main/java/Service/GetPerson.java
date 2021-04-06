package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Service.Result.EventResult;
import Service.Result.PersonResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;

public class GetPerson
{
    public PersonResult result;
    String personID;
    Database db;
    public Gson gson;

    /**
     * Constructor for GetPerson
     * @param personID The ID of the person to be found
     */
    public GetPerson(String personID)
    {
        this.personID = personID;
        db = new Database();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    /**
     * Checks if authtoken is valid and finds person upon success
     * @param authtoken Provided authtoken
     * @throws DataAccessException Issues accessing database
     */
    public void checkAuth(String authtoken) throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();
            db.authTokenDAO = new AuthTokenDAO(connection);
            AuthToken auth = db.authTokenDAO.find(authtoken);

            //if we've made it this far, authtoken exists inside of db
            //now we need to check and see if personID belongs to the user
            //referenced in AuthToken auth object

            //get personID associated event object from db
            db.personDAO = new PersonDAO(connection);
            Person person = db.personDAO.getPerson(personID);

            if (person.getAssociatedUsername().equals(auth.getUsername()))
            {
                db.closeConnection(true);
                result = new PersonResult(person, true);
            }
            else
            {
                throw new DataAccessException("Error: Requested person does not belong to this user.");
            }
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);

            result = new PersonResult(e.getMessage(), false);
            throw new DataAccessException(e.getMessage());
        }
    }
}
