package DAO;

import java.sql.Connection;
import Model.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class EventDAO {
    Connection connection;

    public EventDAO(Connection connection)
    {
        this.connection = connection;
    }

    public boolean openDB()
    {
        return true;
    }
    public boolean closeDB()
    {
        return true;
    }

    public Event getEvent(String eventID)
    {
        return null;
    }

    public ArrayList<Event> getEvents(AuthToken authToken)
    {
        return null;
    }

    public void insert(Event event) throws DataAccessException
    {
        String sql = "INSERT INTO events (event_id, a_UN, person_ID, " +
                "lat, lon, country, city, event_type, year) VALUES(?,?,?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, event.getEventID());//req
            stmt.setString(2, event.getAssociatedUsername());//
            stmt.setString(3, event.getPersonID());//
            stmt.setFloat(4, event.getLatitude());//
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e)
        {
            //System.out.println(e.toString());
            throw new DataAccessException("Error encountered while inserting Event into the database " + e.toString());
        }
    }
    private void update(String sql)
    {
    }
    public void delete(String username) throws DataAccessException
    {
        String sql = "DELETE FROM events WHERE a_UN = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error deleting from events");
        }
    }
}
