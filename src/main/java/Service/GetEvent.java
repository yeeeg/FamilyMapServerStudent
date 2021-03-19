package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;

import java.sql.Connection;

public class GetEvent
{
    String eventID;
    Database db;

    public GetEvent(String eventID)
    {
        this.eventID = eventID;
        db = new Database();
    }

    public void checkAuth(String authtoken) throws DataAccessException
    {
        Connection connection = db.openConnection();
        db.userDAO = new UserDAO(connection);
        db.authTokenDAO = new AuthTokenDAO(connection);

    }

}
