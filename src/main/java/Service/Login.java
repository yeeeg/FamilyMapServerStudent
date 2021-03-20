package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Service.Request.LoginRequest;
import Service.Result.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;

public class Login
{
    public LoginResult result;
    public AuthToken authToken;
    LoginRequest request;
    Database db;
    public Gson gson;

    public Login(LoginRequest request)
    {
        this.result = new LoginResult();
        this.request = request;
        this.db = new Database();

        //create gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    public void loginUser() throws DataAccessException
    {
        try
        {
            Connection connection = db.openConnection();
            //check if username password combo exists inside database and
            //generate authtoken
            db.userDAO = new UserDAO(connection);
            authToken = db.userDAO.login(request.user);

            //insert authtoken into db
            db.authTokenDAO = new AuthTokenDAO(connection);
            db.authTokenDAO.insert(authToken);
            db.closeConnection(true);
            result.success(authToken.getAuthtoken(), authToken.getUsername(), authToken.getPersonID());
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
            result = new LoginResult();
            result.failure(e.toString());
            throw new DataAccessException(e.toString());
        }
    }
}
