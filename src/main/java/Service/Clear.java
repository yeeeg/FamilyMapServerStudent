package Service;

import Service.Result.*;
import Service.*;
import DAO.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;

public class Clear
{
    ClearResult result;
    Database db;
    public Gson gson;

    public Clear()
    {
        this.result = new ClearResult();
        this.db = new Database();

        //create gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    /**
     * Clear the tables from the database using Database.clearTables method
     * @throws DataAccessException States issue with database access
     */
    public void clear() throws DataAccessException
    {
        try
        {
            db.openConnection();
            db.clearTables();
            result.setResult("Clear succeeded.");
            result.success = true;
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            result.setResult("Error: " + e.toString());
            result.success = false;
        }
    }

    /**
     * Get the result of clearing the tables
     * @return result object
     */
    public ClearResult getResult()
    {
        return result;
    }
}
