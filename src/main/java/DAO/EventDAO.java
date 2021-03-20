package DAO;

import java.sql.*;

import Model.*;

import java.util.*;

public class EventDAO {
    Connection connection;

    public EventDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Event find(String eventID) throws DataAccessException
    {
        Event event;
        String sql = "SELECT * FROM events WHERE event_id = ?;";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                event = new Event(rs.getString("event_id"), rs.getString("a_UN"),
                        rs.getString("person_ID"), rs.getFloat("lat"),
                        rs.getFloat("lon"), rs.getString("country"),
                        rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year"));
            }
            else
            {
                throw new DataAccessException("Error: Invalid eventID parameter.");
            }
        }
        catch(SQLException | DataAccessException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return event;
    }

    public ArrayList<Event> getEvents(String a_UN) throws DataAccessException
    {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE a_UN = ?;";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, a_UN);
            rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            if (rs.next())
            {
                events.add(new Event(rs.getString("event_id"), rs.getString("a_UN"),
                        rs.getString("person_ID"), rs.getFloat("lat"),
                        rs.getFloat("lon"), rs.getString("country"),
                        rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year")));
                while (rs.next())
                {
                    events.add(new Event(rs.getString("event_id"), rs.getString("a_UN"),
                            rs.getString("person_ID"), rs.getFloat("lat"),
                            rs.getFloat("lon"), rs.getString("country"),
                            rs.getString("city"), rs.getString("event_type"),
                            rs.getInt("year")));
                }
            }
            else
            {
                //FIXME this is a custom error
                throw new DataAccessException("Error: No events exist for user.");
            }
        }
        catch(SQLException | DataAccessException e)
        {
            throw new DataAccessException(e.getMessage());
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return events;
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
