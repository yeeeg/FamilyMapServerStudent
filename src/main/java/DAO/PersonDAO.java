package DAO;

import java.sql.*;
import java.util.*;
import Model.*;

public class PersonDAO {
    public Connection connection;

    public PersonDAO(Connection connection)
    {
        this.connection = connection;
    }

    public Person getPerson(String personID) throws DataAccessException
    {
        Person person;
        String sql = "SELECT * FROM persons WHERE person_id = ?;";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                person = new Person(rs.getString("person_id"), rs.getString("a_UN"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"), rs.getString("father_id"), rs.getString("mother_id"),
                        rs.getString("spouse_id"));
            }
            else
            {
                //fixme
                throw new DataAccessException("Error: Invalid personID parameter");
            }
        }
        catch(SQLException e)
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

        return person;
    }
    public ArrayList<Person> getPersons(String a_UN) throws DataAccessException
    {
        ArrayList<Person> persons = new ArrayList<>();
        String sql = "SELECT * FROM persons WHERE a_UN = ?;";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, a_UN);
            rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            if (rs.next())
            {
                persons.add(new Person(rs.getString("person_id"), rs.getString("a_UN"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"), rs.getString("father_id"),
                        rs.getString("mother_id"), rs.getString("spouse_id")));

                while (rs.next())
                {
                    persons.add(new Person(rs.getString("person_id"), rs.getString("a_UN"),
                            rs.getString("first_name"), rs.getString("last_name"),
                            rs.getString("gender"), rs.getString("father_id"),
                            rs.getString("mother_id"), rs.getString("spouse_id")));
                }
            }
            else
            {
                //FIXME this is a custom error
                throw new DataAccessException("Error: No persons exist for user.");
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
        return persons;
    }

    public void insert(Person person) throws DataAccessException
    {
        String sql = "INSERT INTO persons (person_id, first_name, last_name, " +
                "gender, a_UN, father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, person.getPersonID());//req
            stmt.setString(2, person.getFirstName());//
            stmt.setString(3, person.getLastName());//
            stmt.setString(4, person.getGender());//

            //non required data
            stmt.setString(5, person.getAssociatedUsername());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e)
        {
            //System.out.println(e.toString());
            throw new DataAccessException("Error encountered while inserting Person into the database " + e.toString());
        }
    }

    public void delete(String username) throws DataAccessException
    {
        String sql = "DELETE FROM persons WHERE a_UN = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Error deleting from persons: " + e.getMessage());
        }
    }
}
